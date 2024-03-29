/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-08-03 17:34:38 UTC)
 * on 2015-10-10 at 21:58:26 UTC 
 * Modify at your own risk.
 */

package com.zhaolongzhong.backend.momentApi.model;

/**
 * Model definition for Duration.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the momentApi. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Duration extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long millis;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long standardDays;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long standardHours;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long standardMinutes;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long standardSeconds;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getMillis() {
    return millis;
  }

  /**
   * @param millis millis or {@code null} for none
   */
  public Duration setMillis(java.lang.Long millis) {
    this.millis = millis;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getStandardDays() {
    return standardDays;
  }

  /**
   * @param standardDays standardDays or {@code null} for none
   */
  public Duration setStandardDays(java.lang.Long standardDays) {
    this.standardDays = standardDays;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getStandardHours() {
    return standardHours;
  }

  /**
   * @param standardHours standardHours or {@code null} for none
   */
  public Duration setStandardHours(java.lang.Long standardHours) {
    this.standardHours = standardHours;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getStandardMinutes() {
    return standardMinutes;
  }

  /**
   * @param standardMinutes standardMinutes or {@code null} for none
   */
  public Duration setStandardMinutes(java.lang.Long standardMinutes) {
    this.standardMinutes = standardMinutes;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getStandardSeconds() {
    return standardSeconds;
  }

  /**
   * @param standardSeconds standardSeconds or {@code null} for none
   */
  public Duration setStandardSeconds(java.lang.Long standardSeconds) {
    this.standardSeconds = standardSeconds;
    return this;
  }

  @Override
  public Duration set(String fieldName, Object value) {
    return (Duration) super.set(fieldName, value);
  }

  @Override
  public Duration clone() {
    return (Duration) super.clone();
  }

}
