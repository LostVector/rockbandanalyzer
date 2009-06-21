package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

import java.util.logging.Logger;

public class ProcessLeaderboards extends BaseAppEngineTaskList {

    private static final Logger log = Logger.getLogger(ProcessLeaderboards.class.getName());

    public ProcessLeaderboards() {
//        _tasks.add(new FlagRawSongsTask());
//        _tasks.add(new ReprocessRawSongsSubtask());
        setId( this.getClass().getName() );
        return;
    }

    @Override
    public void Reset() {
        return;
    }

    @Override
    public Boolean ShouldRun() {
        return true;
    }

    @Override
    public Boolean Finish() {
        return true;
    }


    public class ProcessLeaderboardsSubtask extends BaseAppEngineTask {

        private final Logger log = Logger.getLogger(ProcessLeaderboardsSubtask.class.getName());

        public ProcessLeaderboardsSubtask() {
            setId( this.getClass().getName() );
            return;
        }

        public Boolean Execute() {
            return true;
        }
    }
}
