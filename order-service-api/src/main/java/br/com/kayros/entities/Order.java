package br.com.kayros.entities;


import static br.com.kayros.model.enums.OrderStatusEnum.OPEN;

import br.com.kayros.model.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_order")
public class Order implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 45)
  private String requesterId;

  @Column(nullable = false, length = 45)
  private String customerId;

  @Column(nullable = false, length = 50)
  private String title;

  @Column(nullable = false, length = 3000)
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatusEnum status = OPEN;

  private LocalDateTime createdAt;
  private LocalDateTime closedAt;
}
