package istumapaikat.mn

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import istumapaikat.mn.domain.Room
import istumapaikat.mn.domain.SeatConsumer
import istumapaikat.mn.service.ActionService
import istumapaikat.mn.service.RoomService
import istumapaikat.mn.service.SeatConsumerService
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification


class ActionServiceSpec extends Specification {

    @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared ActionService actionService = new ActionService(embeddedServer.applicationContext.getBean(RoomService), embeddedServer.applicationContext.getBean(SeatConsumerService))

    Room smallRoom
    SeatConsumer singlePerson
    SeatConsumer groupOfPeople

    def setup()
    {
        smallRoom = [roomId:0, seats:1, description:"roomdescription", category:0]
        singlePerson = [count:1, name:"name", description:"description", category:0, strict:false]
        groupOfPeople = [count:4, name:"name", description:"description", category:0, strict:false]
    }

    void "get randomized returns an empty list with empty request"() {
        when:"a request is made with no content in the lists"
        List<Room> rooms = actionService.getRandomizedSeats(null, null)

        then:"No rooms are returned"
        rooms == null
    }

    void "get randomized returns an empty list with empty lists in request"() {
        when:"a request is made with no content in the lists"
        List<Room> rooms = actionService.getRandomizedSeats([], [])

        then:"The returned list is empty"
        rooms.size()==0
    }

    void "people get put into a room when valid content is given"() {
        when:"a request is made with a single room and a single seatconsumer"
        List<Room> rooms = actionService.getRandomizedSeats([smallRoom], [singlePerson])

        then:"The returned list contains one room with no seats left"
        rooms.size()==1
        rooms.get(0).currentSeats==0
    }

    void "trying to fit too many people into the spaces causes an error"() {
        when:"a request is made with a single room and a single seatconsumer"
        actionService.getRandomizedSeats([smallRoom], [groupOfPeople])

        then:"an exception is thrown"
        thrown(Exception)
    }

}