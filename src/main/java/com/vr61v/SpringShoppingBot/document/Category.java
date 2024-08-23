package com.vr61v.SpringShoppingBot.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "categories")
public class Category {

    @Id
    private UUID id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Boolean, name = "is_for_over_eighteen")
    private Boolean isForOverEighteen;

}
