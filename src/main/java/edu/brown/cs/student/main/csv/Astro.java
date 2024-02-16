package edu.brown.cs.student.main.csv;

/**
 * Represents an Astro object with properties for starsign and name.
 */
public class Astro {
  public String starsign;
  public String name;

  /**
   * Constructs an Astro object with the specified name and starsign.
   *
   * @param name     The name associated with the Astro object.
   * @param starsign The starsign associated with the Astro object.
   */
  public Astro(String name, String starsign) {
    this.starsign = starsign;
    this.name = name;
  }

  /**
   * Returns a string representation of the Astro object.
   *
   * @return String representation of the Astro object.
   */
  @Override
  public String toString() {
    return "Astro{" + "starsign='" + starsign + '\'' + ", name='" + name + '\'' + '}';
  }
}
