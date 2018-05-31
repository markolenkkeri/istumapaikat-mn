package istumapaikat.mn.init

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.discovery.event.ServiceStartedEvent
import istumapaikat.mn.service.ActionService

import javax.inject.Singleton

@Slf4j
@CompileStatic
@Singleton
@Requires(notEnv = Environment.TEST)
class AppInit implements ApplicationEventListener<ServiceStartedEvent> {

    final ActionService actionService

    AppInit(ActionService actionService) {
        this.actionService = actionService
    }

    @Override
    void onApplicationEvent(ServiceStartedEvent event) { // <4>
        log.info "Loading csv data"
        actionService.initialize()
    }
}
