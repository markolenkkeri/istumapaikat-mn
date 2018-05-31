package istumapaikat.mn.service

import grails.gorm.transactions.Transactional
import istumapaikat.mn.domain.Room
import istumapaikat.mn.domain.SeatConsumer

import javax.inject.Inject
import javax.inject.Singleton

import static com.xlson.groovycsv.CsvParser.parseCsv

@Singleton

class ActionService {
    SeatConsumerService seatConsumerService
    RoomService roomService

    @Inject
    ActionService(RoomService roomService, SeatConsumerService seatConsumerService)
    {
        this.roomService=roomService
        this.seatConsumerService=seatConsumerService
    }

    @Transactional
    List<Room> getRandomizedSeats(List<Room> rooms, List<SeatConsumer> consumers) {

        def categoryCount = [:].withDefault { 0 }

        println "Randomizing people and teams to rooms. There are " + rooms*.seats?.sum() + " seats and " + consumers*.count?.sum() + " consumers in the files."

        //Sort by count, assign biggest teams first because we're not doing this the fancy way
        consumers = consumers?.sort { a, b -> a.count < b.count ? 1 : -1 }
        def consumerCounts = consumers?.collect { it.count }?.unique()
        Random rand = new Random()

        rooms?.each { room ->
            categoryCount.put(room.category, 0)
            room.currentSeats = room.seats
        }

        consumers?.each {seatConsumer ->
            categoryCount.put(seatConsumer.category, categoryCount.get(seatConsumer.category)+seatConsumer.count)
        }

        //Assign groups of same size
        consumerCounts.each { consumerCount ->
            //Randomize teams/persons of same count so as not to favour order in CSV
            def suitableConsumers = consumers.findAll { it.count == consumerCount }.sort { Math.random() }
            //Find a room for each person/team
            suitableConsumers.each { seatConsumer ->
                def chosenRoom = getSuitableRoom(seatConsumer, rand, categoryCount, rooms)
                chosenRoom.addConsumer(seatConsumer)
                categoryCount.put(seatConsumer.category, categoryCount.get(seatConsumer.category)-seatConsumer.count)
            }
        }

        return rooms

    }
    @Transactional
    private Room getSuitableRoom(SeatConsumer seatConsumer, Random rand, categoryCount, rooms) throws Exception {
        //Find suitable categories of rooms, eg. room categories that have no priority seaters left
        //This could be optimized to calculate if that category has seats left but maybe that's not that important.
        def suitableCategories
        if(seatConsumer.isStrict())suitableCategories = [seatConsumer.category]
        else suitableCategories = ([seatConsumer.category]+categoryCount.findAll { key, value -> value==0}.keySet()).unique()

        //With suitable categories listed, get the suitable rooms
        def suitableRooms = rooms.findAll { it.currentSeats >= seatConsumer.count && suitableCategories.contains(it.category) }
        if (!suitableRooms) { // Well maybe we should just plug this guy somewhere else.. there's no room in suitable categories
            suitableRooms = rooms.findAll { it.currentSeats >= seatConsumer.count }
            if (suitableRooms.size() == 0) {
                println "$seatConsumer could not be assigned a room, please run again.." //Or maybe not, oops
                throw new Exception("No suitable room found!")
            }
        }
        suitableRooms.get(rand.nextInt(suitableRooms.size()))//Select one room from the suitable rooms
    }
    @Transactional
    void initialize()
    {
        //TODO: Make filenames env/conf based
        File roomsFile = new File("rooms.csv")
        File seatConsumersFile = new File("seatconsumers.csv")
        println "Reading CSV files"
        def data = parseCsv(roomsFile?.newReader("UTF-8"))
        data?.each { line ->
            Room room = [roomId: (line.id as Integer), seats: (line.seats as Integer), description: line.description, category: (line.category as Integer)]
            roomService.save(room)
        }

        data = parseCsv(seatConsumersFile?.newReader("UTF-8"))
        data?.each { line ->
            SeatConsumer seatConsumer = [count: (line.count as Integer), description: line.description, category: (line.category as Integer), strict: (line.strict as Integer)==1, name: line.name]
            seatConsumerService.save(seatConsumer)
        }
        println "Read complete"
        println seatConsumerService.count() + " seat consumers added"
        println roomService.count() + " rooms added"
    }

}
