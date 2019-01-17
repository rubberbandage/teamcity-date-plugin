package rubberbandage.teamcity.dateplugin;

import java.text.SimpleDateFormat;
import java.util.Date;

import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SRunningBuild;

import com.intellij.openapi.diagnostic.Logger;

public class DateBuildNumber extends BuildServerAdapter
{
    private final static Logger LOG = Logger.getInstance(DateBuildNumber.class.getName());

    private static final String DATE = "{date}";


    // Constructor
    public DateBuildNumber(SBuildServer aBuildServer)
    {
        System.out.println( "### DateBuildNumber adding listener=" + this );
        LOG.info("### DateBuildNumber adding listener=" + this);

        // Register with TC
        aBuildServer.addListener(this);
    }


    public void buildStarted(SRunningBuild build)
    {
        String buildNumber = build.getBuildNumber();

        // Logging in "teamcity_install_dir\logs\stdout_XXXX.log"
        System.out.println("### DateBuildNumber plugin : buildStarted");
        LOG.info("### DateBuildNumber plugin : buildStarted");

        // If the build number contains the DATE = "{date}" pattern we replace it by the current date.
        if(buildNumber.lastIndexOf(DATE) > -1)
        {
            build.setBuildNumber(buildNumber.replace(DATE, createBuildNumber()));
        }
        else
        {
            System.out.println("### DateBuildNumber plugin, no {date} pattern found");
            LOG.info("### DateBuildNumber plugin, no {date} pattern found");
        }
    }

    public String createBuildNumber()
    {
        Date currentBuild = new Date();
        // Here you can modify the date format
        // Please refer to the javaDoc :
        // http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy-HH'h'mm");
        return sdf.format(currentBuild);
    }
}