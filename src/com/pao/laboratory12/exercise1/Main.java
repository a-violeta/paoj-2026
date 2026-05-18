package com.pao.laboratory12.exercise1;

import com.pao.laboratory12.model.*;
import com.pao.laboratory12.repository.*;
import com.pao.laboratory12.util.DatabaseConnection;
import com.pao.laboratory12.util.SchemaInitializer;

public class Main {

    public static void main(String[] args) {

        try {
            System.out.println("Exercitiul 1");
            // =========================
            // INIT DB + SCHEMA
            // =========================
            var conn = DatabaseConnection.getInstance().getConnection();
            SchemaInitializer.init(conn);

            // =========================
            // REPOSITORIES
            // =========================
            AuthorRepository authorRepo = new AuthorRepository();
            BookRepository bookRepo = new BookRepository();
            ReaderRepository readerRepo = new ReaderRepository();
            LoanRepository loanRepo = new LoanRepository();

            // =========================
            // CREATE
            // =========================
            Author author = new Author("George Orwell", "UK");
            authorRepo.save(author);
            System.out.println("Autor salvat: " + author);

            Book book = new Book("1984", author.getId());
            bookRepo.save(book);
            System.out.println("Carte salvata: " + book);

            Reader reader = new Reader("Ana Popescu", "ana@gmail.com");
            readerRepo.save(reader);
            System.out.println("Reader salvat: " + reader);

            Loan loan = new Loan(book.getId(), reader.getId(), "2026-05-18");
            loanRepo.save(loan);
            System.out.println("Loan salvat: " + loan);

            // =========================
            // FIND ALL
            // =========================
            System.out.println("\n=== AUTORI ===");
            authorRepo.findAll().forEach(System.out::println);

            System.out.println("\n=== CARTI ===");
            bookRepo.findAll().forEach(System.out::println);

            System.out.println("\n=== CITITORI ===");
            readerRepo.findAll().forEach(System.out::println);

            System.out.println("\n=== IMPRUMUTURI ===");
            loanRepo.findAll().forEach(System.out::println);

            // =========================
            // UPDATE
            // =========================
            author.setCountry("England");
            authorRepo.update(author);

            System.out.println("\nAutor dupa update:");
            System.out.println(authorRepo.findById(author.getId()).orElse(null));

            // =========================
            // DELETE
            // =========================
            loanRepo.delete(loan.getId());
            System.out.println("\nLoan sters.");

            System.out.println("\nTEST TERMINAT CU SUCCES");

            DatabaseConnection.getInstance().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}