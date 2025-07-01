package miniprojectjo.domain;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.PagingAndSortingRepository;


@RepositoryRestResource(collectionResourceRel = "books", path = "books")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

    @Query(
        value = "select book " +
                "from Book book " +
                "where(:id is null or book.id = :id) and (:bookName is null or book.bookName like %:bookName%) and (:category is null or book.category like %:category%) and (book.isBestSeller = :isBestSeller) and (:authorName is null or book.authorName like %:authorName%)"
    )
    Book getBookById(Long id, String bookName, String category, Boolean isBestSeller, String authorName);
}
