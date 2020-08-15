package idea.model;

public enum Category {
  BOOK("book"),
  CLOTHING("clothing"),
  ELECTRONIC("electronic"),
  FITNESS("fitness"),
  FURNITURE("furniture"),
  MENTAL("mental"),
  MISC("miscellaneous"),
  OFFICE("office"),
  OUTDOOR("outdoor"),
  PERSONAL_CARE("personal care");

  private final String category;

  Category(String category) {
    this.category = category;
  }
}
