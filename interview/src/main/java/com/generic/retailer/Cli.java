package com.generic.retailer;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Cli implements AutoCloseable {

  public static Cli create(String prompt, BufferedReader reader, BufferedWriter writer, LocalDate date) {
    requireNonNull(prompt);
    requireNonNull(reader);
    requireNonNull(writer);
    return new Cli(prompt, reader, writer, date);
  }

  public static Cli create(BufferedReader reader, BufferedWriter writer) {
    return new Cli(">", reader, writer, LocalDate.now());
  }

  private static final Predicate<String> WHITESPACE = Pattern.compile("^\\s{0,}$").asPredicate();

  private final String prompt;
  private final BufferedReader reader;
  private final BufferedWriter writer;
  private final LocalDate date;

  private Cli(String prompt, BufferedReader reader, BufferedWriter writer, LocalDate date) {
    this.prompt = prompt;
    this.reader = reader;
    this.writer = writer;
    this.date = date;
  }

  private void prompt() throws IOException {
    writeLine(prompt);
  }

  private Optional<String> readLine() throws IOException {
    String line = reader.readLine();
    return line == null || WHITESPACE.test(line) ? Optional.empty() : Optional.of(line);
  }

  private void writeLine(String line) throws IOException {
    writer.write(line);
    writer.newLine();
    writer.flush();
  }

  public void run() throws IOException {
    writeLine("What would you like to buy?");
    prompt();
    Optional<String> line = readLine();
    Trolley trolley = new Trolley();
    line.ifPresent(e -> trolley.addItem(e));
    while (line.isPresent()) {
      writeLine("Would you like anything else?");
      prompt();
      line = readLine();
      line.ifPresent(e -> trolley.addItem(e));
    }
    RecieptRenderer reciept = new RecieptRenderer(trolley, date, writer);
    reciept.render();
    writeLine(String.format("Thank you for visiting Generic Retailer, your total is %s", 0));
  }

  @Override
  public void close() throws Exception {
      reader.close();
      writer.close();
  }

}
