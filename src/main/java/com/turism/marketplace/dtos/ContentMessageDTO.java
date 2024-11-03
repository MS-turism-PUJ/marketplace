package com.turism.marketplace.dtos;

import java.io.Serializable;

import com.turism.marketplace.models.Content;
import com.turism.marketplace.models.Service;
import com.turism.marketplace.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContentMessageDTO implements Serializable {
    private String contentId;

    private String name;

    private String description;

    private String link;

    private String serviceId;

    private String userId;

    public ContentMessageDTO(Content content) {
        this.contentId = content.getContentId();
        this.name = content.getName();
        this.description = content.getDescription();
        this.link = content.getLink();
        this.serviceId = content.getService().getServiceId();
        this.userId = content.getUser().getUserId();
    }

    public Content toContent() {
        Content content = new Content();
        content.setContentId(this.contentId);
        content.setName(this.name);
        content.setDescription(this.description);
        content.setLink(this.link);
        Service service = new Service(serviceId);
        content.setService(service);
        User user = new User(userId);
        content.setUser(user);
        return content;
    }
}

