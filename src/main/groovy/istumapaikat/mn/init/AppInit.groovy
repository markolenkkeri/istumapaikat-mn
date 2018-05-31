package istumapaikat.mn.init

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import istumapaikat.mn.service.ActionService

import javax.inject.Singleton

@Slf4j
@CompileStatic
@Singleton
@Requires(notEnv = Environment.TEST)
class AppInit implements ApplicationEventListener<ServerStartupEvent> {

    final ActionService actionService

    AppInit(ActionService actionService) {
        this.actionService = actionService
    }

    @Override
    void onApplicationEvent(ServerStartupEvent event) { // <4>
        log.info "Loading csv data"
        actionService.initialize()
    }
}
