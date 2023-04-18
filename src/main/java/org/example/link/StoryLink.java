package org.example.link;

import org.example.model.Story;

import java.io.FileNotFoundException;

public class StoryLink extends LinkFactory {

    public StoryLink(Story story) {
        super(story);
    }
    /**
     * @return ссылка на страницу истории с информацией
     */
    @Override
    public String makeLink() throws FileNotFoundException {
        StringBuilder link = new StringBuilder(https);
        link.append(story.makeDictNames().get(story.getName())).append(html);
        return link.toString();
    }

}
