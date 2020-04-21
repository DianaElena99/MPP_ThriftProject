using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SQLite;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSharpServer.Festival.repos
{
    public class SQLiteConnectionFactory : ConnectionFactory
    {
        public override IDbConnection createConnection()
        {
            string connectionString = "URI=file:/Users/user/Desktop/sqlite-tools-win32-x86-3310100/sqlite-tools-win32-x86-3310100/test.db";

            Console.WriteLine("SQLite ---Se deschide o conexiune la  ... {0}", connectionString);
            return new SQLiteConnection(connectionString);

        }
    }
}
