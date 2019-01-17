package rubberbandage.teamcity.dateplugin;

import java.text.SimpleDateFormat;
import java.util.Date;

import jetbrains.buildServer.serverSide.*;

import com.intellij.openapi.diagnostic.Logger;

public class DateBuildNumber extends BuildServerAdapter
{
    private final static Logger LOG = Logger.getInstance(DateBuildNumber.class.getName());

    private static final String DATE = "{date}";
    private static final String BRANCH = "{branch}";

    private Date date = null;

    public DateBuildNumber(SBuildServer aBuildServer)
    {
        System.out.println( "### DateBuildNumber adding listener=" + this );
        LOG.info("### DateBuildNumber adding listener=" + this);

        // Register with TC
        aBuildServer.addListener(this);
    }

    protected void setDate(Date aDate) {
        date = aDate;
    }

    public void buildStarted(SRunningBuild build)
    {
        String buildNumber = build.getBuildNumber();

        if (date == null) {
            date = new Date();
        }

        // Logging in "teamcity_install_dir\logs\stdout_XXXX.log"
        System.out.println("### DateBuildNumber plugin : buildStarted");
        LOG.info("### DateBuildNumber plugin : buildStarted");

        System.out.println(String.format("### DateBuildNumber plugin : buildNumber: %s", buildNumber));
        LOG.info(String.format("### DateBuildNumber plugin : buildNumber: %s", buildNumber));

        // If the build number contains the DATE = "{date}" pattern we replace it by the current date.
        buildNumber = buildNumber.replace(DATE, createBuildNumber(date));

        buildNumber = buildNumber.replace(BRANCH, getBranchName(build.getBranch()));

        System.out.println(String.format("### DateBuildNumber will set : buildNumber: %s", buildNumber));
        LOG.info(String.format("### DateBuildNumber will set : buildNumber: %s", buildNumber));

        build.setBuildNumber(buildNumber);

        System.out.println(String.format("### DateBuildNumber after plugin : buildNumber: %s", build.getBuildNumber()));
        LOG.info(String.format("### DateBuildNumber after plugin : buildNumber: %s", build.getBuildNumber()));
    }

    private String createBuildNumber(Date date)
    {
        // Here you can modify the date format
        // Please refer to the javaDoc :
        // http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.Mdd");
        return sdf.format(date);
    }

    private String getBranchName(Branch branch) {
        return branch == null ? "" : String.format("-%s", branch.getDisplayName());
    }
}