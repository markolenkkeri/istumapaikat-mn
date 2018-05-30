package istumapaikat.mn.controller

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus
import istumapaikat.mn.domain.Room
import istumapaikat.mn.service.RoomService
import istumapaikat.mn.domain.SeatConsumer
import istumapaikat.mn.service.SeatConsumerService

import javax.inject.Inject
import istumapaikat.mn.service.ActionService

@Controller("/action")
@CompileStatic
class ActionController {
    ActionService actionService
    SeatConsumerService seatConsumerService
    RoomService roomService

    @Inject
    ActionController(ActionService actionService, RoomService roomService, SeatConsumerService seatConsumerService)
    {
        this.actionService=actionService
        this.roomService=roomService
        this.seatConsumerService=seatConsumerService
    }

    @Get("/")
    HttpStatus index() {
        return HttpStatus.OK
    }

    @Get("/randomize")
    List<Room> randomize() {
        List<Room> roomList = roomService.findAll()
        List<SeatConsumer> seatConsumerList = seatConsumerService.findAll()
        List<Room> finalRoomList = actionService.getRandomizedSeats(roomList, seatConsumerList)
        return finalRoomList
    }

    @Get("/draftpicks")
    List<SeatConsumer> draftpicks() {
        List<SeatConsumer> seatConsumerList = seatConsumerService.findAll()?.sort { Math.random() }
        return seatConsumerList
    }

    @Get("/initialize")
    Map<String, String> initialize() {
        roomService.findAll()?.each {roomService.delete(it.id)}
        seatConsumerService.findAll()?.each {seatConsumerService.delete(it.id)}
        actionService.initialize()
        Map<String, String> returnMap = [status:"Initialize complete"]
        return returnMap
    }
}
