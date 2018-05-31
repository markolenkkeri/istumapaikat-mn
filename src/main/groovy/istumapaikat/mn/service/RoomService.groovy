package istumapaikat.mn.service

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import istumapaikat.mn.domain.Room

@Service(Room)
abstract class RoomService {

    abstract List<Room> findAll()
    abstract Number count()
    abstract Room save(Room room)
    abstract void delete(Long id)
    @Transactional
    void deleteAll() {
        Room.deleteAll(Room.list())
    }
}
