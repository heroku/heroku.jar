package com.heroku.api.util;

import java.util.ArrayList;

/**
 * @author Joe Kutner on 2/17/17.
 *         Twitter: @codefinger
 */
public class Range<E> extends ArrayList<E> {

  public String nextRange;

  public void setNextRange(String nextRange) {
    this.nextRange = nextRange;
  }

  public String getNextRange() {
    return this.nextRange;
  }

  public Boolean hasNextRange() {
    return this.nextRange != null;
  }
}
