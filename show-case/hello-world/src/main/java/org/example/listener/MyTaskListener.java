package org.example.listener;


import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

public class MyTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        //
        delegateTask.setAssignee("javaboy");
        //
        delegateTask.setVariable("var1", "var1 value");
        //delegateTask.setVariable("manager", "manage value");
    }
}
