package org.example.utils;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlowableUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    public static ProcessEngine getProcessEngine() {
        return context.getBean(ProcessEngine.class);
    }

    public static TaskService getTaskService() {
        return context.getBean(TaskService.class);
    }

    public static RuntimeService getRuntimeService() {
        return context.getBean(RuntimeService.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
