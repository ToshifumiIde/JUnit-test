package net.javaguides.springboot.model;

import jakarta.persistence.*;
import lombok.*;

// lombokのアノテーション
// getterの生成
@Getter
@Setter
// 全フィールドに対する初期化値を引数にとるコンストラクタを生成
@AllArgsConstructor
// デフォルトコンストラクタを自動生成
@NoArgsConstructor
// Builderメソッドを生成
@Builder
// JPA Entityとしてクラス生成するため、アノテーション追加
@Entity
// テーブル生成時の名前を設定
@Table(name ="employees")
public class Employee {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "first_name" , nullable = false)
  private String firstName;
  @Column(name = "last_name" , nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String email;
}
