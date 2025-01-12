package com.jasonpyau.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "links", indexes = @Index(name = "last_updated_unix_time_ind", columnList = "last_updated_unix_time"))
public class Link {

    public static final String LINK_ID_ERROR = "Invalid 'id', link not found.";
    public static final String LINK_NAME_ERROR = "'name' should be between 1-30 characters.";
    public static final String LINK_HREF_ERROR = "'href' should be between 2-500 characters and start with 'http://' or 'https://' or 'mailto:' or '/'.";
    public static final String LINK_SIMPLE_ICONS_ICON_SLUG_ERROR = "'simpleIconsIconSlug' should be between 0-50 characters.";
    public static final String LINK_HEX_FILL_ERROR = "'hexFill' should be either blank or in the form '#xxx' or '#xxxxxx', where x is a hex digit.";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    @Size(min = 1, max = 30, message = LINK_NAME_ERROR)
    @NotBlank(message = LINK_NAME_ERROR)
    private String name;

    @Column(name = "href", nullable = false)
    @Size(min = 2, max = 500, message = LINK_HREF_ERROR)
    @Pattern(regexp = "^((http|https):\\/\\/|mailto:|\\/)(.*)$", message = LINK_HREF_ERROR)
    @NotBlank(message = LINK_HREF_ERROR)
    private String href;

    @Column(name = "simple_icons_icon_slug", nullable = true)
    @Size(max = 50, message = LINK_SIMPLE_ICONS_ICON_SLUG_ERROR)
    // https://www.npmjs.com/package/simple-icons
    // https://github.com/simple-icons/simple-icons/blob/master/slugs.md
    // https://github.com/simple-icons/simple-icons/blob/6.23.0/slugs.md
    private String simpleIconsIconSlug;

    @Column(name = "hex_fill", nullable = true)
    @Size(max = 7, message = LINK_HEX_FILL_ERROR)
    @Pattern(regexp = "^([\\s]*|#([A-Fa-f0-9]{3}|[A-Fa-f0-9]{6}))$", message = LINK_HEX_FILL_ERROR)
    private String hexFill;

    @Column(name = "last_updated_unix_time", nullable = false)
    @JsonIgnore
    private Long lastUpdatedUnixTime;
}
