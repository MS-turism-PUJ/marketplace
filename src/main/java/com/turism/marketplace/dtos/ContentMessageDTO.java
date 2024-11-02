package com.turism.marketplace.dtos;

import java.io.Serializable;

import com.turism.marketplace.models.Content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentMessageDTO implements Serializable {
    private String name;

    private String description;

    private String image;

    private String link;

    private String serviceId;

    public ContentMessageDTO(Content content) {
        this.name = content.getName();
        this.description = content.getDescription();
        this.image = content.getImage();
        this.link = content.getLink();
        this.serviceId = content.getService().getServiceId();
    }

    public Content toContent() {
        Content content = new Content();
        content.setName(this.name);
        content.setDescription(this.description);
        content.setImage(this.image);
        content.setLink(this.link);
        return content;
    }
}

