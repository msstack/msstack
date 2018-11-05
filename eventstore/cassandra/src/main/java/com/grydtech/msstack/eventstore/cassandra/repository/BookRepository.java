package com.grydtech.msstack.eventstore.cassandra.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.grydtech.msstack.eventstore.cassandra.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private static final String TABLE_NAME = "books";
    private static final String TABLE_NAME_BY_TITLE = TABLE_NAME + "ByTitle";

    private Session session;

    public BookRepository(Session session) {
        this.session = session;
    }

    /**
     * Creates the books table.
     */
    public void createTable() {
        final String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + "id uuid PRIMARY KEY, " + "title text," + "author text," + "subject text);";
        session.execute(query);
    }

    /**
     * Creates the books table.
     */
    public void createTableBooksByTitle() {
        final String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BY_TITLE + "(" + "id uuid, " + "title text," + "PRIMARY KEY (title, id));";
        session.execute(query);
    }

    /**
     * Alters the table books and adds an extra column.
     */
    public void alterTableBooks(String columnName, String columnType) {
        final String query = "ALTER TABLE " + TABLE_NAME + " ADD " + columnName + " " + columnType + ";";
        session.execute(query);
    }

    /**
     * Insert a row in the table books.
     *
     * @param book Book
     */
    public void insertBook(Book book) {
        final String query = "INSERT INTO " + TABLE_NAME + "(id, title, author, subject) " + "VALUES (" + book.getId() + ", '" + book.getTitle() + "', '" + book.getAuthor() + "', '" +
                book.getSubject() + "');";
        session.execute(query);
    }

    /**
     * Insert a row in the table booksByTitle.
     *
     * @param book Book
     */
    public void insertBookByTitle(Book book) {
        final String query = "INSERT INTO " + TABLE_NAME_BY_TITLE + "(id, title) " + "VALUES (" + book.getId() + ", '" + book.getTitle() + "');";
        session.execute(query);
    }

    /**
     * Insert a book into two identical tables using a batch query.
     *
     * @param book Book
     */
    public void insertBookBatch(Book book) {
        final String query = "BEGIN BATCH " + "INSERT INTO " + TABLE_NAME + "(id, title, author, subject) " + "VALUES (" + book.getId() + ", '" + book.getTitle() + "', '" + book.getAuthor() +
                "', '" + book.getSubject() + "');" + "INSERT INTO " + TABLE_NAME_BY_TITLE + "(id, title) " + "VALUES (" + book.getId() + ", '" + book.getTitle() + "');" +
                "APPLY BATCH;";
        session.execute(query);
    }

    /**
     * Select book by id.
     *
     * @return Book
     */
    public Book selectByTitle(String title) {
        final String query = "SELECT * FROM " + TABLE_NAME_BY_TITLE + " WHERE title = '" + title + "';";
        ResultSet rs = session.execute(query);
        List<Book> books = new ArrayList<Book>();

        for (Row r : rs) {
            Book s = new Book(r.getUUID("id"), r.getString("title"), null, null);
            books.add(s);
        }

        return books.get(0);
    }

    /**
     * Select all books from books
     *
     * @return List of Books
     */
    public List<Book> selectAll() {
        final String query = "SELECT * FROM " + TABLE_NAME;
        ResultSet rs = session.execute(query);

        List<Book> books = new ArrayList<Book>();

        for (Row r : rs) {
            Book book = new Book(r.getUUID("id"), r.getString("title"), r.getString("author"), r.getString("subject"));
            books.add(book);
        }
        return books;
    }

    /**
     * Select all books from booksByTitle
     *
     * @return List of Books
     */
    public List<Book> selectAllBooksByTitle() {
        final String query = "SELECT * FROM " + TABLE_NAME_BY_TITLE;
        ResultSet rs = session.execute(query);

        List<Book> books = new ArrayList<Book>();

        for (Row r : rs) {
            Book book = new Book(r.getUUID("id"), r.getString("title"), null, null);
            books.add(book);
        }
        return books;
    }

    /**
     * Delete a book by title.
     */
    public void deleteBookByTitle(String title) {
        final String query = "DELETE FROM " + TABLE_NAME_BY_TITLE + " WHERE title = '" + title + "';";
        session.execute(query);
    }

    /**
     * Delete table.
     *
     * @param tableName the databaseName of the table to delete.
     */
    public void deleteTable(String tableName) {
        final String query = "DROP TABLE IF EXISTS " + tableName;
        session.execute(query);
    }
}
