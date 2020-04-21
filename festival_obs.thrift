namespace java UpdateServer
namespace csharp UpdateClient

include "festival.thrift"

service UpdateService{
    void update(1:list<ConcertDTO>);
}