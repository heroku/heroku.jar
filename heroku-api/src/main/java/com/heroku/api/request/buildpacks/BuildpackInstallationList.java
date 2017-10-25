package com.heroku.api.request.buildpacks;

import com.heroku.api.Dyno;
import com.heroku.api.request.Request;
import com.heroku.api.util.Range;

/**
 * @author Joe Kutner on 10/25/17.
 *         Twitter: @codefinger
 */
public class BuildpackInstallationsList implements Request<Range<Dyno>> {

  private final String appName;

}
