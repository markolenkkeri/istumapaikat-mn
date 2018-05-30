package istumapaikat.mn.domain

import grails.gorm.annotation.Entity
import groovy.transform.CompileStatic

@Entity
class Room {
    Integer roomId
    Integer seats
    Integer currentSeats
    String description
    Integer category
    List<SeatConsumer> seatUsageList = new ArrayList<>()

    def addConsumer(SeatConsumer consumer)
    {
        seatUsageList.add(consumer)
        currentSeats -=consumer.count
    }

    @Override
    String toString()
    {
        return "Room $id, $description : $seatUsageList"
    }
    static constraints = {
    }

    static transients = ['seatUsageList', 'currentSeats']
}
