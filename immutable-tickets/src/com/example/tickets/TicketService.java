package com.example.tickets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service layer that creates tickets.
 *
 * CURRENT STATE (BROKEN ON PURPOSE):
 * - creates partially valid objects
 * - mutates after creation (bad for auditability)
 * - validation is scattered & incomplete
 *
 * TODO (student):
 * - After introducing immutable IncidentTicket + Builder, refactor this to stop
 * mutating.
 */
public class TicketService {

    public IncidentTicket createTicket(String id, String reporterEmail, String title) {

        IncidentTicket t = new IncidentTicket.Builder(id, reporterEmail, title)
                .priority("MEDIUM")
                .source("CLI")
                .customerVisible(false)
                .tags(Arrays.asList("NEW"))
                .build();

        return t;
    }

    public IncidentTicket escalateToCritical(IncidentTicket t) {
        List<String> tags = new ArrayList<>(t.getTags());
        tags.add("ESCALATED");
        return t.toBuilder().tags(tags).build();
    }

    public IncidentTicket assign(IncidentTicket t, String assigneeEmail) {
        Validation.requireEmail(assigneeEmail, assigneeEmail);
        return t.toBuilder().assigneeEmail(assigneeEmail).build();
    }
}
