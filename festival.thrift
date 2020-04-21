namespace java org.festival.common
namespace csharp Festival

struct Concert{
    1: i32 id
    2: i32 artistID
    3: i32 locationID
    4: string date
    5: i32 seats
}

struct ConcertDTO{
    1: i32 concertID
    2: string artist
    3: string location 
    4: string date
    5: i32 seatsSold
    6: i32 seatsTotal
    7: i32 seatsFree
}

struct Bilet{
    1: i32 concertID
    2: string customer
    3: i32 seats
}

struct User{
    1: string username
    2: string passwd
}

service THRIFTService{
    bool Login(1: User user, 2: i32 port)
    bool Logout(1: User user, 2: i32 port)
    void SellBilet(1: Bilet b)
    list<ConcertDTO> getConcerts()
    list<ConcertDTO> getConcertsByDate(1: string data)
    void notifyClients()
}

service UpdateService{
    void update(1:list<ConcertDTO> lista)
}