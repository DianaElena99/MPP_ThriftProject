using CSharpServer.Festival.repos;
using Festival;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Thrift.Protocol;
using Thrift.Transport;

namespace CSharpServer.Festival.services
{
    public class ServiceReal : THRIFTService.ISync
    {
        private BiletRepo biletRepo;
        private ConcertRepo concertRepo;
        private UserRepo userRepo;

        IDictionary<User, FestivalObserver> loggedUsers;
        private List<UpdateService.Client> updateClients;

        public ServiceReal(BiletRepo biletRepo, ConcertRepo concertRepo, UserRepo userRepo)
        {
            this.biletRepo = biletRepo;
            this.concertRepo = concertRepo;
            this.userRepo = userRepo;
            loggedUsers = new ConcurrentDictionary<User, FestivalObserver>();
            updateClients = new List<UpdateService.Client>();
        }

        public List<ConcertDTO> getConcerts()
        {
            return concertRepo.cautaConcerteDetalii();
        }

        public List<ConcertDTO> getConcertsByDate(string data)
        {
            return concertRepo.cautaConcerteDetalii().Where(x => x.Date.Contains(data)).ToList();
        }

        public bool Login(User user, int port)
        {
            int valid = userRepo.CheckLoginCredentials(user.Username, user.Passwd);
            if (valid != -1)
            {
                TTransport transport = new TSocket("localhost", port);
                transport.Open();
                TProtocol proto = new TBinaryProtocol(transport);
                UpdateService.Client client = new UpdateService.Client(proto);
                this.updateClients.Add(client);
                return true;
            }
            return false;
        }

        public bool Logout(User user, int port)
        {
            return true;
        }

        public void notifyClients()
        {
            List<ConcertDTO> lista = concertRepo.cautaConcerteDetalii();
            foreach(var client in this.updateClients)
            {
                try
                {
                    Console.WriteLine("updating client : " + client.ToString());
                    client.update(lista);
                    Console.WriteLine("DONE");
                }
                catch(Exception e)
                {
                    //
                }
            }
        }

        public void SellBilet(Bilet b)
        {
            try
            {
                Console.WriteLine("Selling ticket in ServiceReal.cs");

                biletRepo.saveBilet(b);
                int nrLocuriNou = b.Seats + concertRepo.findOne(b.ConcertID).Seats;
                concertRepo.updateLocuriLibere(b.ConcertID, nrLocuriNou);
                List<ConcertDTO> L = concertRepo.cautaConcerteDetalii();
                L.Where(x => x.ConcertID == b.ConcertID)
                    .ToList()
                    .ForEach(x => Console.WriteLine(x.ToString()));

                notifyClients();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}
