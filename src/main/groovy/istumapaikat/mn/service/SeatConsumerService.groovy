package istumapaikat.mn.service

import grails.gorm.services.Service
import grails.gorm.services.Where
import istumapaikat.mn.domain.SeatConsumer

@Service(SeatConsumer)
interface SeatConsumerService {

    List<SeatConsumer> findAll()
    Number count()
    SeatConsumer save(SeatConsumer seatConsumer)
    void delete(Long id)

    @Where({id>=0})
    void deleteAll()
}


