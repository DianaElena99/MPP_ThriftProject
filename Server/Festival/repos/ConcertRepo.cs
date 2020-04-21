using Festival;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSharpServer.Festival.repos
{
    public class ConcertRepo
    {
        public SQLiteConnectionFactory utile;

        public ConcertRepo()
        {
            //logg.Info("\nCreated a concert repo \n");
            utile = new SQLiteConnectionFactory();
        }


        public List<ConcertDTO> cautaConcerteDetalii()
        {
            List<ConcertDTO> concerte = new List<ConcertDTO>();
            String cmd = "select c.ID, a.Nume as ARTIST , L.Nume as LOCATIE, c.Data as DATA, c.NrLocuri as LOCURI_OCUP, L.NrLocuri as NrLocuriTot from Concert c inner join Locatie L on L.ID = c.Locatie inner join Artist a on a.ID = c.Artist";
            IDbConnection conexiune = utile.createConnection();
            conexiune.Open();
            using (IDbCommand com = conexiune.CreateCommand())
            {
                com.CommandText = cmd;
                using (var reader = com.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        int p1 = reader.GetInt32(0);
                        string art = reader.GetString(1);
                        string d = reader.GetString(3);
                        string loc = reader.GetString(2);
                        int locuriO = reader.GetInt32(4);
                        int nrLoc = reader.GetInt32(5);
                        ConcertDTO conDTO = new ConcertDTO();
                        conDTO.ConcertID = p1;
                        conDTO.Artist = art;
                        conDTO.Location = loc;
                        conDTO.SeatsSold = locuriO;
                        conDTO.SeatsTotal = nrLoc;
                        conDTO.SeatsFree = nrLoc - locuriO;
                        conDTO.Date = d;
                        concerte.Add(conDTO);
                    }
                }
            }
            return concerte;
        }

        public Concert findOne(int i)
        {
            IDbConnection conexiune = utile.createConnection();
            conexiune.Open();
            using (IDbCommand comm = conexiune.CreateCommand())
            {
                comm.CommandText = "select * from Concert where ID = @ID";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@ID";
                paramId.Value = i;
                comm.Parameters.Add(paramId);
                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        int idArt = dataR.GetInt16(1);
                        int idLoc = dataR.GetInt16(2);
                        int nrSc = dataR.GetInt16(3);
                        string data = dataR.GetString(4);
                        DateTime dataAux = DateTime.Parse(data);
                        Concert con = new Concert();
                        con.ArtistID = idArt;
                        con.LocationID = idLoc;
                        con.Date = data;
                        con.Seats = nrSc;
                        return con;
                    }
                }

            }
            return null;
        }


        public void updateLocuriLibere(int idConcert, int nrBilete)
        {
            IDbConnection conexiune = utile.createConnection();
            conexiune.Open();

            using (IDbCommand comm = conexiune.CreateCommand())
            {
                comm.CommandText = "UPDATE CONCERT Set NrLocuri = @p1 WHERE ID = @p2";
                Console.WriteLine("Entered concert repo > updateLocuriLibere");

                IDbDataParameter param1 = comm.CreateParameter();
                param1.Value = idConcert;
                param1.ParameterName = "@p2";
                comm.Parameters.Add(param1);

                IDbDataParameter param2 = comm.CreateParameter();
                param2.Value = nrBilete;
                param2.ParameterName = "@p1";
                comm.Parameters.Add(param2);

                comm.ExecuteNonQuery();
            }
        }

        public List<ConcertDTO> cautaConcerteData(string data)
        {
            List<ConcertDTO> concerte = new List<ConcertDTO>();
            String cmd = "select c.ID, a.Nume as ARTIST , L.Nume as LOCATIE, c.Data as DATA, c.NrLocuri as LOCURI_OCUP, L.NrLocuri as NrLocuriTot from Concert c inner join Locatie L on L.LocID = c.Locatie inner join Artist a on a.ID = c.Artist";
            IDbConnection conexiune = utile.createConnection();
            conexiune.Open();
            using (IDbCommand com = conexiune.CreateCommand())
            {
                com.CommandText = cmd;
                using (var reader = com.ExecuteReader())
                {
                    while (reader.Read())
                    {
                        int p1 = reader.GetInt32(0);
                        string art = reader.GetString(1);
                        DateTime d = reader.GetDateTime(3);
                        if (d.ToShortDateString() != data)
                            continue;
                        string loc = reader.GetString(2);
                        int locuriO = reader.GetInt32(4);
                        int nrLoc = reader.GetInt32(5);
                        ConcertDTO conDTO = new ConcertDTO();
                        conDTO.Artist = art;
                        conDTO.Location = loc;
                        conDTO.SeatsSold = locuriO;
                        conDTO.SeatsTotal = nrLoc;
                        conDTO.SeatsFree = nrLoc - locuriO;
                        conDTO.Date = data;
                        concerte.Add(conDTO);
                    }
                }
            }
            return concerte;

        }

    }
}
