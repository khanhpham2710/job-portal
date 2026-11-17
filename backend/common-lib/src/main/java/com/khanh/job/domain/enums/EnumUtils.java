package com.khanh.job.domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public final class EnumUtils {

  private EnumUtils() {}

  // Get all enum constants
  private static <E extends Enum<E>> Stream<E> getAllEnum(Class<E> enumClass) {
    return Arrays.stream(enumClass.getEnumConstants());
  }

  // Find enum by code
  public static <E extends Enum<E> & BaseEnum> Optional<E> fromCode(
      Class<E> enumClass, String code) {

    return getAllEnum(enumClass)
        .filter(e -> Objects.equals(String.valueOf(e.getCode()), code))
        .findFirst();
  }

  // Find enum by name
  public static <E extends Enum<E>> Optional<E> fromName(Class<E> enumClass, String name) {

    return getAllEnum(enumClass).filter(e -> Objects.equals(e.name(), name)).findFirst();
  }

  // Find enum by description
  public static <E extends Enum<E> & BaseEnum> Optional<E> fromDescription(
      Class<E> enumClass, String description) {

    return getAllEnum(enumClass)
        .filter(e -> Objects.equals(e.getDescription(), description))
        .findFirst();
  }

  // Get all codes
  public static <E extends Enum<E> & BaseEnum> List<String> getCodes(Class<E> enumClass) {

    return getAllEnum(enumClass).map(BaseEnum::getCode).toList();
  }

  // Get all descriptions
  public static <E extends Enum<E> & BaseEnum> List<String> getDescriptions(Class<E> enumClass) {

    return getAllEnum(enumClass).map(BaseEnum::getDescription).toList();
  }

  // Get all names
  public static <E extends Enum<E>> List<String> getNames(Class<E> enumClass) {

    return getAllEnum(enumClass).map(Enum::name).toList();
  }

  // Check valid name
  public static <E extends Enum<E>> boolean isValidName(Class<E> enumClass, String name) {

    return fromName(enumClass, name).isPresent();
  }

  // Check valid code
  public static <E extends Enum<E> & BaseEnum> boolean isValidCode(
      Class<E> enumClass, String code) {

    return fromCode(enumClass, code).isPresent();
  }

  // Check valid description
  public static <E extends Enum<E> & BaseEnum> boolean isValidDescription(
      Class<E> enumClass, String description) {

    return fromDescription(enumClass, description).isPresent();
  }
}
