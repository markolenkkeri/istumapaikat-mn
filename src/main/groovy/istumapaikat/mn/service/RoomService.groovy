package istumapaikat.mn.service

import grails.gorm.services.Service
import istumapaikat.mn.domain.Room

@Service(Room)
interface RoomService {

    List<Room> findAll()
    Number count()
    Room save(Room room)
    void delete(Long id)
}
