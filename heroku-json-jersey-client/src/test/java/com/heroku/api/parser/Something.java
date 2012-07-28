package com.heroku.api.parser;

/**
 * @author Ryan Brainard
 */
public class Something {

    private String name;

    public Something() {
    }

    public Something(String name) {
        this.name = name;
    }

    private void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Something something = (Something) o;

        if (name != null ? !name.equals(something.name) : something.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Something{" +
                "name='" + name + '\'' +
                '}';
    }
}
