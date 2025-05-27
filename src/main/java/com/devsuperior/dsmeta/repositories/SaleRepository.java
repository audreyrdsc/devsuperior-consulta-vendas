package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    //Busca dos dados com Join Fetch - evitar N+1
    @Query(value = "SELECT obj FROM Sale obj JOIN FETCH obj.seller")
    List<Sale> searchAll();

    //Busca dos dados de Sale e Saller para REPORT, conforme parâmetros opcionais (PAGINADA)
    @Query(value = "SELECT obj FROM Sale obj JOIN FETCH obj.seller " +
                   "WHERE obj.date BETWEEN :minDate AND :maxDate " +
                   "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))",
           countQuery = "SELECT COUNT(obj) FROM Sale obj " +
                   "WHERE obj.date BETWEEN :minDate AND :maxDate " +
                   "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Sale> searchReport(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);

    //Busca dos dados de Sale e Saller para SUMMARY, conforme parâmetros opcionais (LISTADA)
    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(obj.seller.name, SUM(obj.amount)) " +
            "FROM Sale obj WHERE obj.date BETWEEN :initialDate AND :finalDate " +
            "GROUP BY obj.seller.name")
    List<SaleSummaryDTO> searchSummary(LocalDate initialDate, LocalDate finalDate);

}
//SELECT TB_SELLER.NAME AS NAME, SUM(TB_SALES.AMOUNT) AS TOTAL
//FROM TB_SALES
//JOIN TB_SELLER ON TB_SALES.SELLER_ID = TB_SELLER.ID
//WHERE TB_SALES.DATE BETWEEN '2024-01-01' AND '2024-02-28'
//GROUP BY TB_SELLER.NAME;