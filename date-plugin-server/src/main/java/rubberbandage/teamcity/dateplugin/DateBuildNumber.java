package rubberbandage.teamcity.dateplugin;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.serverSide.Branch;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SRunningBuild;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateBuildNumber extends BuildServerAdapter
{
    private final static Logger LOG = Logger.getInstance(DateBuildNumber.class.getName());

    private static final String DATE = "{date}";
    private static final String BRANCH = "{branch}";
    private static final String META = "{meta}";

    private static final Integer OCTOPACK_MAX_LENGTH = 20;

    // Here you can modify the date format
    // Please refer to the javaDoc :
    // http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html
    private static final String DATE_FORMAT = "yyyy.Mdd";

    private static final String ADD_LISTENER = "### DateBuildNumber adding listener=";
    private static final String BUILD_STARTED = "### DateBuildNumber plugin : buildStarted";
    private static final String BRANCH_NOT_CONFIGURED = "### DateBuildNumber branch: branch is currently null, have you configured branch specification";

    private Date date = null;

    public DateBuildNumber(SBuildServer aBuildServer)
    {
        System.out.println( ADD_LISTENER + this );
        LOG.info(ADD_LISTENER + this);

        // Register with TC
        aBuildServer.addListener(this);
    }

    public void buildStarted(SRunningBuild build)
    {
        // Logging in "teamcity_install_dir\logs\stdout_XXXX.log"
        System.out.println(BUILD_STARTED);
        LOG.info(BUILD_STARTED);

        String buildNumber = build.getBuildNumber();

        // If the build number contains the DATE = "{date}" pattern we replace it by the current date.
        buildNumber = buildNumber.replace(DATE, createBuildNumber(date));
        buildNumber = buildNumber.replace(BRANCH, createBranchName(build.getBranch()));
        buildNumber = buildNumber.replace(META, createMetaInformation(build.getBranch()));

        build.setBuildNumber(buildNumber);
    }

    private String createBuildNumber(Date date)
    {
        Date tempDate = (date == null) ? new Date() : date;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(tempDate);
    }

    private String createBranchName(Branch branch) {
        if (branch == null) {
            System.out.println(BRANCH_NOT_CONFIGURED);
            LOG.info(BRANCH_NOT_CONFIGURED);

            return "";
        } else {
            String branchName = branch.getDisplayName().replaceAll("\\/", "_");
            branchName = StringUtils.left(branchName, OCTOPACK_MAX_LENGTH);
            return branch.isDefaultBranch() ? "" : String.format("-%s", branchName);
        }
    }

    private String createMetaInformation(Branch branch) {
        if (branch == null) {
            System.out.println(BRANCH_NOT_CONFIGURED);
            LOG.info(BRANCH_NOT_CONFIGURED);

            return "";
        } else {
            String branchName = branch.getDisplayName().replaceAll("\\/", "_");
            return branch.isDefaultBranch() ? "+master" : String.format("+%s", branchName);
        }
    }

    protected void setDate(Date aDate) {
        date = aDate;
    }
}