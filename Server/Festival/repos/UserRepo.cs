using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSharpServer.Festival.repos
{
    public class UserRepo
    {
        public ConnectionFactory utile;

        public UserRepo()
        {
            utile = new SQLiteConnectionFactory();
        }

        public int CheckLoginCredentials(string user, string pass)
        {
            int rez = -1;
            try
            {
                IDbConnection con = utile.createConnection();

                using (IDbCommand cmd = con.CreateCommand())
                {
                    con.Open();
                    cmd.CommandText = "SELECT ID FROM USER WHERE NUME = @p1 and PAROLA = @p2";
                    var par1 = cmd.CreateParameter();
                    par1.Value = user;
                    par1.ParameterName = "@p1";
                    cmd.Parameters.Add(par1);

                    var par2 = cmd.CreateParameter();
                    par2.Value = pass;
                    par2.ParameterName = "@p2";
                    cmd.Parameters.Add(par2);

                    int aux = (int)cmd.ExecuteScalar();
                    if (aux > 0)
                    {
                        rez = aux;
                        Console.Write("USER REPO - Utilizator gasit\n");
                    }
                    else
                    {
                        Console.WriteLine("USER REPO - Autentificare nereusita\n");
                    }
                }
            }
            catch (Exception e)
            {
                Console.Write(e.Message);
            }
            return rez;

        }

    }
}
