using Festival;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSharpServer.Festival.repos
{
    public class BiletRepo
    {
        public ConnectionFactory factory;
        public BiletRepo()
        {
            factory = new SQLiteConnectionFactory();
        }

        public void saveBilet(Bilet b)
        {
            try
            {
                IDbConnection con = factory.createConnection();
                con.Open();
                using (var com = con.CreateCommand())
                {
                    com.CommandText = "INSERT INTO BILET VALUES (@p1, @p2, @p3)";
                    var param = com.CreateParameter();
                    param.ParameterName = "@p1";
                    param.Value = b.Customer;
                    com.Parameters.Add(param);

                    var param2 = com.CreateParameter();
                    param2.ParameterName = "@p2";
                    param2.Value = b.ConcertID;
                    com.Parameters.Add(param2);

                    var param3 = com.CreateParameter();
                    param3.ParameterName = "@p3";
                    param3.Value = b.Seats;
                    com.Parameters.Add(param3);


                    com.ExecuteNonQuery();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}
