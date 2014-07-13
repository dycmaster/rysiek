package dycmaster.rysiek.triggers2.logics.implementations;


import dycmaster.rysiek.triggers2.TimeOnlyTrigger;
import dycmaster.rysiek.triggers2.TimeTriggerLogic;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Ths one gives a short spike when the cron time
 * criteria is reached.
 * Good for:
 *  -alarm clock
 *  -playing news
 *  -making coffee
 */
public class SingleShotCronTimeLogic extends TimeTriggerLogic {

    private final String cronString;
    private static final int spikeTime = 500;

    public SingleShotCronTimeLogic(String description, TimeOnlyTrigger trigger, String cronString) {
        super(description, trigger);
        this.cronString = cronString;
        try {
            run();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    private void run() throws SchedulerException {
        updateTriggerState(false);      //start from false

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDetail job = newJob(JobToDo.class)
                .withIdentity("job1", "group1")
                .build();

        job.getJobDataMap().put("trigger", getTrigger());

        CronTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(cronSchedule(cronString))
                .build();

        sched.scheduleJob(job, trigger);
        sched.start();
    }


    @DisallowConcurrentExecution
    public static class JobToDo implements Job{

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

            JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
            TimeOnlyTrigger trigger = (TimeOnlyTrigger) data.get("trigger");

            trigger.updateTriggerStateBasedOnLogic(true);

            try {
                Thread.sleep(spikeTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            trigger.updateTriggerStateBasedOnLogic(false);
        }
    }

}
