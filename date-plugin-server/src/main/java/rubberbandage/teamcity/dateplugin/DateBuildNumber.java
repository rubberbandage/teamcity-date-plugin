package rubberbandage.teamcity.dateplugin;

import java.text.SimpleDateFormat;
import java.util.Date;

import jetbrains.buildServer.serverSide.Branch;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SRunningBuild;

import com.intellij.openapi.diagnostic.Logger;

public class DateBuildNumber extends BuildServerAdapter
{
    private final static Logger LOG = Logger.getInstance(DateBuildNumber.class.getName());

    private static final String DATE = "{date}";
    private static final String BRANCH = "{branch}";

    private Date date;

    public DateBuildNumber(SBuildServer aBuildServer)
    {
        date = new Date();
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
        Branch branch = build.getBranch();

        String newBuildName = buildNumber;

        // Logging in "teamcity_install_dir\logs\stdout_XXXX.log"
        System.out.println("### DateBuildNumber plugin : buildStarted");
        LOG.info("### DateBuildNumber plugin : buildStarted");

        // If the build number contains the DATE = "{date}" pattern we replace it by the current date.
        if(buildNumber.lastIndexOf(DATE) > -1)
        {
           newBuildName = newBuildName.replace(DATE, createBuildNumber());
        }
        else
        {
            System.out.println("### DateBuildNumber plugin, no {date} pattern found");
            LOG.info("### DateBuildNumber plugin, no {date} pattern found");
        }

        if(branch.isDefaultBranch()) {
            newBuildName = newBuildName.replace(BRANCH, "");
        } else {
            newBuildName = newBuildName.replace(BRANCH, "-" + branch.getDisplayName());
        }

        build.setBuildNumber(newBuildName);
    }

    private String createBuildNumber()
    {
        // Here you can modify the date format
        // Please refer to the javaDoc :
        // http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.Mdd");
        return sdf.format(date);
    }
}