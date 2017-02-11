// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package com.ibm.icu.impl.number;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.ibm.icu.impl.number.Parse.IProperties;
import com.ibm.icu.impl.number.Parse.ParseMode;
import com.ibm.icu.impl.number.formatters.BigDecimalMultiplier;
import com.ibm.icu.impl.number.formatters.CompactDecimalFormat;
import com.ibm.icu.impl.number.formatters.CurrencyFormat;
import com.ibm.icu.impl.number.formatters.CurrencyFormat.CurrencyStyle;
import com.ibm.icu.impl.number.formatters.MagnitudeMultiplier;
import com.ibm.icu.impl.number.formatters.MeasureFormat;
import com.ibm.icu.impl.number.formatters.PaddingFormat;
import com.ibm.icu.impl.number.formatters.PaddingFormat.PaddingLocation;
import com.ibm.icu.impl.number.formatters.PositiveDecimalFormat;
import com.ibm.icu.impl.number.formatters.PositiveNegativeAffixFormat;
import com.ibm.icu.impl.number.formatters.ScientificFormat;
import com.ibm.icu.impl.number.rounders.IntervalRounder;
import com.ibm.icu.impl.number.rounders.MagnitudeRounder;
import com.ibm.icu.impl.number.rounders.SignificantDigitsRounder;
import com.ibm.icu.text.CompactDecimalFormat.CompactStyle;
import com.ibm.icu.text.CurrencyPluralInfo;
import com.ibm.icu.text.MeasureFormat.FormatWidth;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.Currency.CurrencyUsage;
import com.ibm.icu.util.MeasureUnit;

public class Properties
    implements Cloneable,
        PositiveDecimalFormat.IProperties,
        PositiveNegativeAffixFormat.IProperties,
        MagnitudeMultiplier.IProperties,
        ScientificFormat.IProperties,
        MeasureFormat.IProperties,
        CompactDecimalFormat.IProperties,
        PaddingFormat.IProperties,
        BigDecimalMultiplier.IProperties,
        CurrencyFormat.IProperties,
        Parse.IProperties,
        IntervalRounder.IProperties,
        MagnitudeRounder.IProperties,
        SignificantDigitsRounder.IProperties {

  private static final Properties DEFAULT = new Properties();

  // TODO: Change all the CharSequence types to immutable Strings?
  // This would also require either retiring CurrencyPluralInfo or copying it upon receipt.
  // These two changes would make all fields in Properties immutable.

  /*--------------------------------------------------------------------------------------------+/
  /| IMPORTANT!                                                                                 |/
  /| WHEN ADDING A NEW PROPERTY, ADD IT HERE, IN #clear(), IN #equals(), AND IN #hashCode().    |/
  /|                                                                                            |/
  /| The unit test PropertiesTest will catch if you forget to add it to #clear() or #equals(),  |/
  /| but it will NOT catch if you forget to add it to #hashCode().                              |/
  /+--------------------------------------------------------------------------------------------*/

  private boolean alwaysShowDecimal;
  private boolean alwaysShowPlusSign;
  private CompactStyle compactStyle;
  private Currency currency;
  private CurrencyPluralInfo currencyPluralInfo;
  private CurrencyStyle currencyStyle;
  private CurrencyUsage currencyUsage;
  private boolean decimalPatternMatchRequired;
  private int exponentDigits;
  private boolean exponentShowPlusSign;
  private int groupingSize;
  private int magnitudeMultiplier;
  private MathContext mathContext;
  private int maximumFractionDigits;
  private int maximumIntegerDigits;
  private int maximumSignificantDigits;
  private FormatWidth measureFormatWidth;
  private MeasureUnit measureUnit;
  private int minimumFractionDigits;
  private int minimumGroupingDigits;
  private int minimumIntegerDigits;
  private int minimumSignificantDigits;
  private BigDecimal multiplier;
  private CharSequence negativePrefix;
  private CharSequence negativePrefixPattern;
  private CharSequence negativeSuffix;
  private CharSequence negativeSuffixPattern;
  private PaddingLocation paddingLocation;
  private CharSequence paddingString;
  private int paddingWidth;
  //  private boolean parseCurrency;
  private boolean parseCaseSensitive;
  private boolean parseIgnoreExponent;
  private boolean parseIntegerOnly;
  private ParseMode parseMode;
  private boolean parseToBigDecimal;
  private CharSequence positivePrefix;
  private CharSequence positivePrefixPattern;
  private CharSequence positiveSuffix;
  private CharSequence positiveSuffixPattern;
  private BigDecimal roundingInterval;
  private RoundingMode roundingMode;
  private int secondaryGroupingSize;
  private boolean significantDigitsOverride;

  /*--------------------------------------------------------------------------------------------+/
  /| IMPORTANT!                                                                                 |/
  /| WHEN ADDING A NEW PROPERTY, ADD IT HERE, IN #clear(), IN #equals(), AND IN #hashCode().    |/
  /|                                                                                            |/
  /| The unit test PropertiesTest will catch if you forget to add it to #clear() or #equals(),  |/
  /| but it will NOT catch if you forget to add it to #hashCode().                              |/
  /+--------------------------------------------------------------------------------------------*/

  public Properties() {
    clear();
  }

  private Properties _clear() {
    alwaysShowDecimal = DEFAULT_ALWAYS_SHOW_DECIMAL;
    alwaysShowPlusSign = DEFAULT_ALWAYS_SHOW_PLUS_SIGN;
    compactStyle = DEFAULT_COMPACT_STYLE;
    currency = DEFAULT_CURRENCY;
    currencyPluralInfo = DEFAULT_CURRENCY_PLURAL_INFO;
    currencyStyle = DEFAULT_CURRENCY_STYLE;
    currencyUsage = DEFAULT_CURRENCY_USAGE;
    decimalPatternMatchRequired = DEFAULT_DECIMAL_PATTERN_MATCH_REQUIRED;
    exponentDigits = DEFAULT_EXPONENT_DIGITS;
    exponentShowPlusSign = DEFAULT_EXPONENT_SHOW_PLUS_SIGN;
    groupingSize = DEFAULT_GROUPING_SIZE;
    magnitudeMultiplier = DEFAULT_MAGNITUDE_MULTIPLIER;
    mathContext = DEFAULT_MATH_CONTEXT;
    maximumFractionDigits = DEFAULT_MAXIMUM_FRACTION_DIGITS;
    maximumIntegerDigits = DEFAULT_MAXIMUM_INTEGER_DIGITS;
    maximumSignificantDigits = DEFAULT_MAXIMUM_SIGNIFICANT_DIGITS;
    measureFormatWidth = DEFAULT_MEASURE_FORMAT_WIDTH;
    measureUnit = DEFAULT_MEASURE_UNIT;
    minimumFractionDigits = DEFAULT_MINIMUM_FRACTION_DIGITS;
    minimumGroupingDigits = DEFAULT_MINIMUM_GROUPING_DIGITS;
    minimumIntegerDigits = DEFAULT_MINIMUM_INTEGER_DIGITS;
    minimumSignificantDigits = DEFAULT_MINIMUM_SIGNIFICANT_DIGITS;
    multiplier = DEFAULT_MULTIPLIER;
    negativePrefix = DEFAULT_NEGATIVE_PREFIX;
    negativePrefixPattern = DEFAULT_NEGATIVE_PREFIX_PATTERN;
    negativeSuffix = DEFAULT_NEGATIVE_SUFFIX;
    negativeSuffixPattern = DEFAULT_NEGATIVE_SUFFIX_PATTERN;
    paddingLocation = DEFAULT_PADDING_LOCATION;
    paddingString = DEFAULT_PADDING_STRING;
    paddingWidth = DEFAULT_PADDING_WIDTH;
    //    parseCurrency = DEFAULT_PARSE_CURRENCY;
    parseCaseSensitive = DEFAULT_PARSE_CASE_SENSITIVE;
    parseIgnoreExponent = DEFAULT_PARSE_IGNORE_EXPONENT;
    parseIntegerOnly = DEFAULT_PARSE_INTEGER_ONLY;
    parseMode = DEFAULT_PARSE_MODE;
    parseToBigDecimal = DEFAULT_PARSE_TO_BIG_DECIMAL;
    positivePrefix = DEFAULT_POSITIVE_PREFIX;
    positivePrefixPattern = DEFAULT_POSITIVE_PREFIX_PATTERN;
    positiveSuffix = DEFAULT_POSITIVE_SUFFIX;
    positiveSuffixPattern = DEFAULT_POSITIVE_SUFFIX_PATTERN;
    roundingInterval = DEFAULT_ROUNDING_INTERVAL;
    roundingMode = DEFAULT_ROUNDING_MODE;
    secondaryGroupingSize = DEFAULT_SECONDARY_GROUPING_SIZE;
    significantDigitsOverride = DEFAULT_SIGNIFICANT_DIGITS_OVERRIDE_MAXIMUM_DIGITS;
    return this;
  }

  private boolean _equals(Properties other) {
    boolean eq = true;
    eq = eq && _equalsHelper(alwaysShowDecimal, other.alwaysShowDecimal);
    eq = eq && _equalsHelper(alwaysShowPlusSign, other.alwaysShowPlusSign);
    eq = eq && _equalsHelper(compactStyle, other.compactStyle);
    eq = eq && _equalsHelper(currency, other.currency);
    eq = eq && _equalsHelper(currencyPluralInfo, other.currencyPluralInfo);
    eq = eq && _equalsHelper(currencyStyle, other.currencyStyle);
    eq = eq && _equalsHelper(currencyUsage, other.currencyUsage);
    eq = eq && _equalsHelper(decimalPatternMatchRequired, other.decimalPatternMatchRequired);
    eq = eq && _equalsHelper(exponentDigits, other.exponentDigits);
    eq = eq && _equalsHelper(exponentShowPlusSign, other.exponentShowPlusSign);
    eq = eq && _equalsHelper(groupingSize, other.groupingSize);
    eq = eq && _equalsHelper(magnitudeMultiplier, other.magnitudeMultiplier);
    eq = eq && _equalsHelper(mathContext, other.mathContext);
    eq = eq && _equalsHelper(maximumFractionDigits, other.maximumFractionDigits);
    eq = eq && _equalsHelper(maximumIntegerDigits, other.maximumIntegerDigits);
    eq = eq && _equalsHelper(maximumSignificantDigits, other.maximumSignificantDigits);
    eq = eq && _equalsHelper(measureFormatWidth, other.measureFormatWidth);
    eq = eq && _equalsHelper(measureUnit, other.measureUnit);
    eq = eq && _equalsHelper(minimumFractionDigits, other.minimumFractionDigits);
    eq = eq && _equalsHelper(minimumGroupingDigits, other.minimumGroupingDigits);
    eq = eq && _equalsHelper(minimumIntegerDigits, other.minimumIntegerDigits);
    eq = eq && _equalsHelper(minimumSignificantDigits, other.minimumSignificantDigits);
    eq = eq && _equalsHelper(multiplier, other.multiplier);
    eq = eq && _equalsHelper(negativePrefix, other.negativePrefix);
    eq = eq && _equalsHelper(negativePrefixPattern, other.negativePrefixPattern);
    eq = eq && _equalsHelper(negativeSuffix, other.negativeSuffix);
    eq = eq && _equalsHelper(negativeSuffixPattern, other.negativeSuffixPattern);
    eq = eq && _equalsHelper(paddingLocation, other.paddingLocation);
    eq = eq && _equalsHelper(paddingString, other.paddingString);
    eq = eq && _equalsHelper(paddingWidth, other.paddingWidth);
    //    eq = eq && _equalsHelper(parseCurrency, other.parseCurrency);
    eq = eq && _equalsHelper(parseCaseSensitive, other.parseCaseSensitive);
    eq = eq && _equalsHelper(parseIgnoreExponent, other.parseIgnoreExponent);
    eq = eq && _equalsHelper(parseIntegerOnly, other.parseIntegerOnly);
    eq = eq && _equalsHelper(parseMode, other.parseMode);
    eq = eq && _equalsHelper(parseToBigDecimal, other.parseToBigDecimal);
    eq = eq && _equalsHelper(positivePrefix, other.positivePrefix);
    eq = eq && _equalsHelper(positivePrefixPattern, other.positivePrefixPattern);
    eq = eq && _equalsHelper(positiveSuffix, other.positiveSuffix);
    eq = eq && _equalsHelper(positiveSuffixPattern, other.positiveSuffixPattern);
    eq = eq && _equalsHelper(roundingInterval, other.roundingInterval);
    eq = eq && _equalsHelper(roundingMode, other.roundingMode);
    eq = eq && _equalsHelper(secondaryGroupingSize, other.secondaryGroupingSize);
    eq = eq && _equalsHelper(significantDigitsOverride, other.significantDigitsOverride);
    return eq;
  }

  private boolean _equalsHelper(boolean mine, boolean theirs) {
    return mine == theirs;
  }

  private boolean _equalsHelper(CharSequence mine, CharSequence theirs) {
    // We have to write this helper separately from Object because the CharSequence interface
    // doesn't require the #equals() method to work as one would expect.
    // This also allows a null CharSequence to equal an empty CharSequence.
    if (mine == theirs) return true;
    if (mine == null && theirs.length() != 0) return false;
    if (theirs == null && mine.length() != 0) return false;
    if (mine.length() != theirs.length()) return false;
    for (int i = 0; i < mine.length(); i++) {
      if (mine.charAt(i) != theirs.charAt(i)) return false;
    }
    return true;
  }

  private boolean _equalsHelper(int mine, int theirs) {
    return mine == theirs;
  }

  private boolean _equalsHelper(Object mine, Object theirs) {
    if (mine == theirs) return true;
    if (mine == null) return false;
    return mine.equals(theirs);
  }

  private int _hashCode() {
    int hashCode = 0;
    hashCode ^= _hashCodeHelper(alwaysShowDecimal);
    hashCode ^= _hashCodeHelper(alwaysShowPlusSign);
    hashCode ^= _hashCodeHelper(compactStyle);
    hashCode ^= _hashCodeHelper(currency);
    hashCode ^= _hashCodeHelper(currencyPluralInfo);
    hashCode ^= _hashCodeHelper(currencyStyle);
    hashCode ^= _hashCodeHelper(currencyUsage);
    hashCode ^= _hashCodeHelper(decimalPatternMatchRequired);
    hashCode ^= _hashCodeHelper(exponentDigits);
    hashCode ^= _hashCodeHelper(exponentShowPlusSign);
    hashCode ^= _hashCodeHelper(groupingSize);
    hashCode ^= _hashCodeHelper(magnitudeMultiplier);
    hashCode ^= _hashCodeHelper(mathContext);
    hashCode ^= _hashCodeHelper(maximumFractionDigits);
    hashCode ^= _hashCodeHelper(maximumIntegerDigits);
    hashCode ^= _hashCodeHelper(maximumSignificantDigits);
    hashCode ^= _hashCodeHelper(measureFormatWidth);
    hashCode ^= _hashCodeHelper(measureUnit);
    hashCode ^= _hashCodeHelper(minimumFractionDigits);
    hashCode ^= _hashCodeHelper(minimumGroupingDigits);
    hashCode ^= _hashCodeHelper(minimumIntegerDigits);
    hashCode ^= _hashCodeHelper(minimumSignificantDigits);
    hashCode ^= _hashCodeHelper(multiplier);
    hashCode ^= _hashCodeHelper(negativePrefix);
    hashCode ^= _hashCodeHelper(negativePrefixPattern);
    hashCode ^= _hashCodeHelper(negativeSuffix);
    hashCode ^= _hashCodeHelper(negativeSuffixPattern);
    hashCode ^= _hashCodeHelper(paddingLocation);
    hashCode ^= _hashCodeHelper(paddingString);
    hashCode ^= _hashCodeHelper(paddingWidth);
    //    hashCode ^= _hashCodeHelper(parseCurrency);
    hashCode ^= _hashCodeHelper(parseCaseSensitive);
    hashCode ^= _hashCodeHelper(parseIgnoreExponent);
    hashCode ^= _hashCodeHelper(parseIntegerOnly);
    hashCode ^= _hashCodeHelper(parseMode);
    hashCode ^= _hashCodeHelper(parseToBigDecimal);
    hashCode ^= _hashCodeHelper(positivePrefix);
    hashCode ^= _hashCodeHelper(positivePrefixPattern);
    hashCode ^= _hashCodeHelper(positiveSuffix);
    hashCode ^= _hashCodeHelper(positiveSuffixPattern);
    hashCode ^= _hashCodeHelper(roundingInterval);
    hashCode ^= _hashCodeHelper(roundingMode);
    hashCode ^= _hashCodeHelper(secondaryGroupingSize);
    hashCode ^= _hashCodeHelper(significantDigitsOverride);
    return hashCode;
  }

  private int _hashCodeHelper(boolean value) {
    return value ? 1 : 0;
  }

  private int _hashCodeHelper(int value) {
    return value * 13;
  }

  private int _hashCodeHelper(Object value) {
    if (value == null) return 0;
    return value.hashCode();
  }

  public Properties clear() {
    return _clear();
  }

  /** Creates and returns a shallow copy of the property bag. */
  @Override
  public Properties clone() {
    // super.clone() returns a shallow copy.
    try {
      return (Properties) super.clone();
    } catch (CloneNotSupportedException e) {
      // Should never happen since super is Object
      throw new UnsupportedOperationException(e);
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) return false;
    if (this == other) return true;
    if (!(other instanceof Properties)) return false;
    return _equals((Properties) other);
  }

  @Override
  public boolean getAlwaysShowDecimal() {
    return alwaysShowDecimal;
  }

  @Override
  public boolean getAlwaysShowPlusSign() {
    return alwaysShowPlusSign;
  }

  @Override
  public CompactStyle getCompactStyle() {
    return compactStyle;
  }

  @Override
  public Currency getCurrency() {
    return currency;
  }

  @Override
  @Deprecated
  public CurrencyPluralInfo getCurrencyPluralInfo() {
    return currencyPluralInfo;
  }

  @Override
  public CurrencyStyle getCurrencyStyle() {
    return currencyStyle;
  }

  @Override
  public CurrencyUsage getCurrencyUsage() {
    return currencyUsage;
  }

  @Override
  public boolean getDecimalPatternMatchRequired() {
    return decimalPatternMatchRequired;
  }

  @Override
  public int getExponentDigits() {
    return exponentDigits;
  }

  @Override
  public boolean getExponentShowPlusSign() {
    return exponentShowPlusSign;
  }

  @Override
  public int getGroupingSize() {
    return groupingSize;
  }

  @Override
  public int getMagnitudeMultiplier() {
    return magnitudeMultiplier;
  }

  @Override
  public MathContext getMathContext() {
    return mathContext;
  }

  @Override
  public int getMaximumFractionDigits() {
    return maximumFractionDigits;
  }

  @Override
  public int getMaximumIntegerDigits() {
    return maximumIntegerDigits;
  }

  @Override
  public int getMaximumSignificantDigits() {
    return maximumSignificantDigits;
  }

  @Override
  public FormatWidth getMeasureFormatWidth() {
    return measureFormatWidth;
  }

  @Override
  public MeasureUnit getMeasureUnit() {
    return measureUnit;
  }

  @Override
  public int getMinimumFractionDigits() {
    return minimumFractionDigits;
  }

  @Override
  public int getMinimumGroupingDigits() {
    return minimumGroupingDigits;
  }

  @Override
  public int getMinimumIntegerDigits() {
    return minimumIntegerDigits;
  }

  @Override
  public int getMinimumSignificantDigits() {
    return minimumSignificantDigits;
  }

  @Override
  public BigDecimal getMultiplier() {
    return multiplier;
  }

  @Override
  public CharSequence getNegativePrefix() {
    return negativePrefix;
  }

  @Override
  public CharSequence getNegativePrefixPattern() {
    return negativePrefixPattern;
  }

  @Override
  public CharSequence getNegativeSuffix() {
    return negativeSuffix;
  }

  @Override
  public CharSequence getNegativeSuffixPattern() {
    return negativeSuffixPattern;
  }

  //  @Override
  //  public boolean getParseCurrency() {
  //    return parseCurrency;
  //  }

  @Override
  public PaddingLocation getPaddingLocation() {
    return paddingLocation;
  }

  @Override
  public CharSequence getPaddingString() {
    return paddingString;
  }

  @Override
  public int getPaddingWidth() {
    return paddingWidth;
  }

  @Override
  public boolean getParseCaseSensitive() {
    return parseCaseSensitive;
  }

  /* (non-Javadoc)
   * @see com.ibm.icu.impl.number.Parse.IProperties#getParseIgnoreExponent()
   */
  @Override
  public boolean getParseIgnoreExponent() {
    return parseIgnoreExponent;
  }

  /* (non-Javadoc)
   * @see com.ibm.icu.impl.number.Parse.IProperties#getParseIntegerOnly()
   */
  @Override
  public boolean getParseIntegerOnly() {
    return parseIntegerOnly;
  }

  /* (non-Javadoc)
   * @see com.ibm.icu.impl.number.Parse.IProperties#getParseMode()
   */
  @Override
  public ParseMode getParseMode() {
    return parseMode;
  }

  @Override
  public boolean getParseToBigDecimal() {
    return parseToBigDecimal;
  }

  @Override
  public CharSequence getPositivePrefix() {
    return positivePrefix;
  }

  @Override
  public CharSequence getPositivePrefixPattern() {
    return positivePrefixPattern;
  }

  @Override
  public CharSequence getPositiveSuffix() {
    return positiveSuffix;
  }

  @Override
  public CharSequence getPositiveSuffixPattern() {
    return positiveSuffixPattern;
  }

  @Override
  public BigDecimal getRoundingInterval() {
    return roundingInterval;
  }

  @Override
  public RoundingMode getRoundingMode() {
    return roundingMode;
  }

  @Override
  public int getSecondaryGroupingSize() {
    return secondaryGroupingSize;
  }

  @Override
  public boolean getSignificantDigitsOverride() {
    return significantDigitsOverride;
  }

  @Override
  public int hashCode() {
    return _hashCode();
  }

  @Override
  public Properties setAlwaysShowDecimal(boolean alwaysShowDecimal) {
    this.alwaysShowDecimal = alwaysShowDecimal;
    return this;
  }

  @Override
  public Properties setAlwaysShowPlusSign(boolean alwaysShowPlusSign) {
    this.alwaysShowPlusSign = alwaysShowPlusSign;
    return this;
  }

  @Override
  public Properties setCompactStyle(CompactStyle compactStyle) {
    this.compactStyle = compactStyle;
    return this;
  }

  @Override
  public Properties setCurrency(Currency currency) {
    this.currency = currency;
    return this;
  }

  @Override
  @Deprecated
  public Properties setCurrencyPluralInfo(CurrencyPluralInfo currencyPluralInfo) {
    this.currencyPluralInfo = currencyPluralInfo;
    return this;
  }

  @Override
  public Properties setCurrencyStyle(CurrencyStyle currencyStyle) {
    this.currencyStyle = currencyStyle;
    return this;
  }

  @Override
  public Properties setCurrencyUsage(CurrencyUsage currencyUsage) {
    this.currencyUsage = currencyUsage;
    return this;
  }

  @Override
  public Properties setDecimalPatternMatchRequired(boolean decimalPatternMatchRequired) {
    this.decimalPatternMatchRequired = decimalPatternMatchRequired;
    return this;
  }

  @Override
  public Properties setExponentDigits(int exponentDigits) {
    this.exponentDigits = exponentDigits;
    return this;
  }

  @Override
  public Properties setExponentShowPlusSign(boolean exponentShowPlusSign) {
    this.exponentShowPlusSign = exponentShowPlusSign;
    return this;
  }

  @Override
  public Properties setGroupingSize(int groupingSize) {
    this.groupingSize = groupingSize;
    return this;
  }

  @Override
  public Properties setMagnitudeMultiplier(int magnitudeMultiplier) {
    this.magnitudeMultiplier = magnitudeMultiplier;
    return this;
  }

  @Override
  public Properties setMathContext(MathContext mathContext) {
    this.mathContext = mathContext;
    return this;
  }

  @Override
  public Properties setMaximumFractionDigits(int maximumFractionDigits) {
    this.maximumFractionDigits = maximumFractionDigits;
    return this;
  }

  @Override
  public Properties setMaximumIntegerDigits(int maximumIntegerDigits) {
    this.maximumIntegerDigits = maximumIntegerDigits;
    return this;
  }

  @Override
  public Properties setMaximumSignificantDigits(int maximumSignificantDigits) {
    this.maximumSignificantDigits = maximumSignificantDigits;
    return this;
  }

  @Override
  public Properties setMeasureFormatWidth(FormatWidth measureFormatWidth) {
    this.measureFormatWidth = measureFormatWidth;
    return this;
  }

  @Override
  public Properties setMeasureUnit(MeasureUnit measureUnit) {
    this.measureUnit = measureUnit;
    return this;
  }

  @Override
  public Properties setMinimumFractionDigits(int minimumFractionDigits) {
    this.minimumFractionDigits = minimumFractionDigits;
    return this;
  }

  @Override
  public Properties setMinimumGroupingDigits(int minimumGroupingDigits) {
    this.minimumGroupingDigits = minimumGroupingDigits;
    return this;
  }

  @Override
  public Properties setMinimumIntegerDigits(int minimumIntegerDigits) {
    this.minimumIntegerDigits = minimumIntegerDigits;
    return this;
  }

  @Override
  public Properties setMinimumSignificantDigits(int minimumSignificantDigits) {
    this.minimumSignificantDigits = minimumSignificantDigits;
    return this;
  }

  @Override
  public Properties setMultiplier(BigDecimal multiplier) {
    this.multiplier = multiplier;
    return this;
  }

  //  @Override
  //  public Properties setParseCurrency(boolean parseCurrency) {
  //    this.parseCurrency = parseCurrency;
  //    return this;
  //  }

  @Override
  public Properties setNegativePrefix(CharSequence negativePrefix) {
    this.negativePrefix = negativePrefix;
    return this;
  }

  @Override
  public Properties setNegativePrefixPattern(CharSequence negativePrefixPattern) {
    this.negativePrefixPattern = negativePrefixPattern;
    return this;
  }

  @Override
  public Properties setNegativeSuffix(CharSequence negativeSuffix) {
    this.negativeSuffix = negativeSuffix;
    return this;
  }

  @Override
  public Properties setNegativeSuffixPattern(CharSequence negativeSuffixPattern) {
    this.negativeSuffixPattern = negativeSuffixPattern;
    return this;
  }

  @Override
  public Properties setPaddingLocation(PaddingLocation paddingLocation) {
    this.paddingLocation = paddingLocation;
    return this;
  }

  @Override
  public Properties setPaddingString(CharSequence paddingString) {
    this.paddingString = paddingString;
    return this;
  }

  @Override
  public Properties setPaddingWidth(int paddingWidth) {
    this.paddingWidth = paddingWidth;
    return this;
  }

  @Override
  public IProperties setParseCaseSensitive(boolean parseCaseSensitive) {
    this.parseCaseSensitive = parseCaseSensitive;
    return this;
  }

  /* (non-Javadoc)
   * @see com.ibm.icu.impl.number.Parse.IProperties#setParseIgnoreExponent(boolean)
   */
  @Override
  public Properties setParseIgnoreExponent(boolean parseIgnoreExponent) {
    this.parseIgnoreExponent = parseIgnoreExponent;
    return this;
  }

  /* (non-Javadoc)
   * @see com.ibm.icu.impl.number.Parse.IProperties#setParseIntegerOnly(boolean)
   */
  @Override
  public Properties setParseIntegerOnly(boolean parseIntegerOnly) {
    this.parseIntegerOnly = parseIntegerOnly;
    return this;
  }

  /* (non-Javadoc)
   * @see com.ibm.icu.impl.number.Parse.IProperties#setParseMode(com.ibm.icu.impl.number.Parse.ParseMode)
   */
  @Override
  public Properties setParseMode(ParseMode parseMode) {
    this.parseMode = parseMode;
    return this;
  }

  @Override
  public Properties setParseToBigDecimal(boolean parseToBigDecimal) {
    this.parseToBigDecimal = parseToBigDecimal;
    return this;
  }

  @Override
  public Properties setPositivePrefix(CharSequence positivePrefix) {
    this.positivePrefix = positivePrefix;
    return this;
  }

  @Override
  public Properties setPositivePrefixPattern(CharSequence positivePrefixPattern) {
    this.positivePrefixPattern = positivePrefixPattern;
    return this;
  }

  @Override
  public Properties setPositiveSuffix(CharSequence positiveSuffix) {
    this.positiveSuffix = positiveSuffix;
    return this;
  }

  @Override
  public Properties setPositiveSuffixPattern(CharSequence positiveSuffixPattern) {
    this.positiveSuffixPattern = positiveSuffixPattern;
    return this;
  }

  @Override
  public Properties setRoundingInterval(BigDecimal interval) {
    this.roundingInterval = interval;
    return this;
  }

  @Override
  public Properties setRoundingMode(RoundingMode roundingMode) {
    this.roundingMode = roundingMode;
    return this;
  }

  @Override
  public Properties setSecondaryGroupingSize(int secondaryGroupingSize) {
    this.secondaryGroupingSize = secondaryGroupingSize;
    return this;
  }

  @Override
  public Properties setSignificantDigitsOverride(boolean significantDigitsOverrideMaximumDigits) {
    this.significantDigitsOverride = significantDigitsOverrideMaximumDigits;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("<Properties");
    Field[] fields = Properties.class.getDeclaredFields();
    for (Field field : fields) {
      Object myValue, defaultValue;
      try {
        myValue = field.get(this);
        defaultValue = field.get(DEFAULT);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        continue;
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        continue;
      }
      if (myValue == null && defaultValue == null) {
        continue;
      } else if (myValue == null || defaultValue == null) {
        result.append(" " + field.getName() + ":" + myValue);
      } else if (!myValue.equals(defaultValue)) {
        result.append(" " + field.getName() + ":" + myValue);
      }
    }
    result.append(">");
    return result.toString();
  }
}
