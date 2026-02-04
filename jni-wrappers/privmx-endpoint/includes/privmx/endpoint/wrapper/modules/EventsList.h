#ifndef PRIVMXENDPOINT_EVENTSLIST_H
#define PRIVMXENDPOINT_EVENTSLIST_H

#include "privmx/endpoint/wrapper/parsers/parser.h"

class EventsList {
private:
    EventsList() {
        addParser(parseEvent);
    }

public:
    static EventsList &instance(){
        static EventsList eventsList;
        return eventsList;
    }

    void addParser( jobject (*fun)(JniContextUtils&, std::shared_ptr<privmx::endpoint::core::Event>)){
        list_of_parsers.push_back(fun);
    }

    jobject getEvent(
            JniContextUtils& ctx,
            std::shared_ptr<privmx::endpoint::core::Event> event
    ) {
        int size = list_of_parsers.size() -1;
        for(int i=size; i>=0; --i)
        {
            jobject res = list_of_parsers[i](ctx, event);
            if (res != nullptr) {
                return res;
            }
        }

        return nullptr;
    }

    std::vector< jobject (*)(JniContextUtils &ctx, std::shared_ptr<privmx::endpoint::core::Event> event)> list_of_parsers;
};


#endif //PRIVMXENDPOINT_EVENTSLIST_H
