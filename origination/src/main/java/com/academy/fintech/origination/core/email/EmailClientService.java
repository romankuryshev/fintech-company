package com.academy.fintech.origination.core.email;

import com.academy.fintech.origination.core.service.application.db.application.Application;

public interface EmailClientService {

    void sendApplicationStatusNotification(Application application);
}
