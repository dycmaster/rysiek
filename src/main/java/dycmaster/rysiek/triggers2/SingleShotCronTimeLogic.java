package dycmaster.rysiek.triggers2;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;

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
    private static Logger _log = org.slf4j.LoggerFactory.getLogger(SingleShotCronTimeLogic.class);

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
        Scheduler sched = sf.getScheduler() ;

        JobDetail job = newJob(JobToDo.class)
                .withIdentity("job_"+System.currentTimeMillis(), "group1")
                .build();

        job.getJobDataMap().put("trigger", getTrigger());


        CronTrigger trigger = newTrigger()
                .withIdentity("trigger_"+System.currentTimeMillis(), "group1")
                .withSchedule(cronSchedule(cronString))
                .build();

        sched.scheduleJob(job, trigger);
        sched.start();
        _log.trace("trigger's next fire = " + trigger.getNextFireTime());
        _log.trace("trigger's start = " + trigger.getStartTime());
        _log.trace("trigger's cron = " + trigger.getCronExpression());
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
                _log.trace("nextFireTime = " + jobExecutionContext.getTrigger().getNextFireTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            trigger.updateTriggerStateBasedOnLogic(false);
        }
    }

}
