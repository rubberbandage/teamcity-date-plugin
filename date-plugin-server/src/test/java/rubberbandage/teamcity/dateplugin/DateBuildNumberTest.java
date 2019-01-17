package rubberbandage.teamcity.dateplugin;

import jetbrains.buildServer.serverSide.Branch;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SRunningBuild;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DateBuildNumberTest {

    private SRunningBuild runningBuild;
    private SBuildServer buildServer;
    private DateBuildNumber dateBuildNumber;

    @Before
    public void setUp() {
        runningBuild = mock(SRunningBuild.class);
        buildServer = mock(SBuildServer.class);

        Date date = new LocalDateTime("2019-01-17").toDate();

        dateBuildNumber = new DateBuildNumber(buildServer);
        dateBuildNumber.setDate(date);

        when(runningBuild.getBuildNumber()).thenReturn("{date}{branch}");
    }

    @Test
    public void shouldStillRunWhenNoSubstitutionsAreUsed() {
        when(runningBuild.getBuildNumber()).thenReturn("random text for the build number");
        when(runningBuild.getBranch()).thenReturn(new MasterBranch());

        dateBuildNumber.buildStarted(runningBuild);

        verify(runningBuild).setBuildNumber("random text for the build number");
    }

    @Test
    public void shouldReplacePlaceholderWithDate() {
        when(runningBuild.getBranch()).thenReturn(null);

        dateBuildNumber.buildStarted(runningBuild);

        verify(runningBuild).setBuildNumber("2019.117");
    }

    @Test
    public void shouldReplaceEveryInstanceOfDate() {
        when(runningBuild.getBuildNumber()).thenReturn("{date}.{date}");
        when(runningBuild.getBranch()).thenReturn(null);

        dateBuildNumber.buildStarted(runningBuild);

        verify(runningBuild).setBuildNumber("2019.117.2019.117");
    }

    @Test
    public void shouldNotAddBranchNameWhenBranchIsNull() {
        when(runningBuild.getBranch()).thenReturn(null);

        dateBuildNumber.buildStarted(runningBuild);

        verify(runningBuild).setBuildNumber("2019.117");
    }

    @Test
    public void shouldNotAddBranchNameWhenMaster() {
        when(runningBuild.getBranch()).thenReturn(new MasterBranch());

        dateBuildNumber.buildStarted(runningBuild);

        verify(runningBuild).setBuildNumber("2019.117");
    }

    @Test
    public void shouldAddBranchNameWhenNotMaster() {
        when(runningBuild.getBranch()).thenReturn(new FeatureBranch());

        dateBuildNumber.buildStarted(runningBuild);

        verify(runningBuild).setBuildNumber("2019.117-feature");
    }

    private class MasterBranch implements Branch {
        @NotNull
        @Override
        public String getName() {
            return "master";
        }

        @NotNull
        @Override
        public String getDisplayName() {
            return "<default>";
        }

        @Override
        public boolean isDefaultBranch() {
            return true;
        }
    }

    private class FeatureBranch implements Branch {
        @NotNull
        @Override
        public String getName() {
            return "feature";
        }

        @NotNull
        @Override
        public String getDisplayName() {
            return "feature";
        }

        @Override
        public boolean isDefaultBranch() {
            return false;
        }
    }
}
