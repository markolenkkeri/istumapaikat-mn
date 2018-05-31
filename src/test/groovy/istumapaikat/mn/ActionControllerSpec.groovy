package istumapaikat.mn;

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import istumapaikat.mn.domain.Room
import istumapaikat.mn.domain.SeatConsumer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class ActionControllerSpec extends Specification {

    @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared @AutoCleanup RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())


    void "test index"() {
        given:
        HttpResponse response = client.toBlocking().exchange("/action")

        expect:
        response.status == HttpStatus.OK
    }

    void "test uninitialized draftpicks"() {
        given:
        String seatConsumers = client.toBlocking().retrieve("/action/draftpicks")

        expect:
        seatConsumers=="[]"
    }

    void "test uninitialized random order listing"() {
        given:"A web request arrives"
        String rooms = client.toBlocking().retrieve("/action/randomize")

        expect:"Returned list is empty"
        rooms =="[]"

    }
}
