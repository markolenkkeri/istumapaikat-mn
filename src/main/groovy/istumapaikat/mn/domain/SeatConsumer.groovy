package istumapaikat.mn.domain

import grails.gorm.annotation.Entity

@Entity
class SeatConsumer {

    int count
    String description
    String name
    int category
    boolean strict

    @Override
    String toString()
    {
        if(count>1) return "$description ($count persons)"
        return "$description"
    }

    static constraints = {
        name nullable:true
    }
}
