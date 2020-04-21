using CSharpServer.Festival.repos;
using CSharpServer.Festival.services;
using Festival;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using Thrift.Server;
using Thrift.Transport;

namespace CSharpServer
{
    static class StartServer
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            ServiceReal service = GetService();

            THRIFTService.Processor processor = new THRIFTService.Processor(service);
            TServerTransport serverTransport = new TServerSocket(9095);
            TServer server = new TThreadedServer(processor, serverTransport);
            Console.WriteLine("Created transport --- Starting server on port 9095");
            server.Serve();
        }

        private static ServiceReal GetService()
        {
            var userRepo = new UserRepo();
            userRepo.CheckLoginCredentials("admin", "admin");

            var concertRepo = new ConcertRepo();
            var concerte = concertRepo.cautaConcerteDetalii();
            var biletRepo = new BiletRepo();
            ServiceReal service = new ServiceReal(biletRepo, concertRepo, userRepo);
            var concerteFiltrate = service.getConcertsByDate("2020-05-06");
            foreach (ConcertDTO c in concerte)
            {
                Console.WriteLine(c.Location + " " + c.Artist + " " + c.Date);
            }
            foreach (ConcertDTO con in concerteFiltrate)
            {
                Console.WriteLine(con.Location);
            }
            return service;
        }
    }
}
