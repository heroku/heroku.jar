package com.heroku.api;

public class TeamApp extends App {

  private static final long serialVersionUID = 1L;

  boolean joined;
  boolean locked;
  Team team;

  public TeamApp withTeam(Team team) {
    TeamApp newApp = copy();
    newApp.team = team;
    return newApp;
  }

  @Override
  public TeamApp on(Heroku.Stack stack) {
    TeamApp newApp = copy();
    newApp.stack = new App.Stack(stack);
    return newApp;
  }

  private TeamApp copy() {
    TeamApp copy = new TeamApp();
    copy.name = this.name;
    copy.stack = this.stack;
    copy.team = this.team;
    return copy;
  }

  public boolean isJoined() {
    return joined;
  }

  public void setJoined(boolean joined) {
    this.joined = joined;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

}
