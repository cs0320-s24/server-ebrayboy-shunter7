package edu.brown.cs.student.main;

public class Astro {
  public String starsign;
  public String name;

  public Astro(String name, String starsign) {
    this.starsign = starsign;
    this.name = name;
  }

  @Override
  public String toString() {
    return "Astro{" + "starsign='" + starsign + '\'' + ", name='" + name + '\'' + '}';
  }
}
