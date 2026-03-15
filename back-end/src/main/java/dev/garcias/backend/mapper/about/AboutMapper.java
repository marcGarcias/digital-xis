package dev.garcias.backend.mapper.about;

import dev.garcias.backend.dto.cms.CMSAboutDTO;
import dev.garcias.backend.dto.cms.CMSRichTextChildDTO;
import dev.garcias.backend.dto.cms.CMSRichTextDTO;
import dev.garcias.backend.dto.response.about.AboutResponse;
import dev.garcias.backend.mapper.shared.ImageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface AboutMapper {

    @Mapping(source = "descricao", target = "description")
    @Mapping(source = "imagemSuperiorEsquerdaDesktop", target = "topLeftImageDesktop")
    @Mapping(source = "imagemInferiorEsquerdaDesktop", target = "bottomLeftImageDesktop")
    @Mapping(source = "imagemDireitaDesktop", target = "rightImageDesktop")
    @Mapping(source = "imagemInferiorDesktop", target = "bottomImageDesktop")
    @Mapping(source = "imagemSuperiorEsquerdaMobile", target = "topLeftImageMobile")
    @Mapping(source = "imagemInferiorEsquerdaMobile", target = "bottomLeftImageMobile")
    @Mapping(source = "imagemDireitaMobile", target = "rightImageMobile")
    @Mapping(source = "imagemInferiorMobile", target = "bottomImageMobile")
    AboutResponse toAboutResponse(CMSAboutDTO cmsAboutDTO);

    default String richTextToString(List<CMSRichTextDTO> blocks) {
        if (blocks == null) return null;

        String html = blocks.stream()
                .map(block -> {
                    String text = block.children().stream()
                            .map(child -> {
                                String t = child.text() == null ? "" : child.text();

                                t = t.replace("\n", "<br>");

                                if (Boolean.TRUE.equals(child.bold()))          t = "<strong>" + t + "</strong>";
                                if (Boolean.TRUE.equals(child.italic()))        t = "<em>" + t + "</em>";
                                if (Boolean.TRUE.equals(child.underline()))     t = "<u>" + t + "</u>";
                                if (Boolean.TRUE.equals(child.strikethrough())) t = "<s>" + t + "</s>";
                                if (Boolean.TRUE.equals(child.code()))          t = "<code>" + t + "</code>";

                                return switch (child.type()) {
                                    case "list-item" -> "<li>" + t + "</li>";
                                    default -> t;
                                };
                            })
                            .collect(Collectors.joining());

                    return switch (block.type()) {
                        case "paragraph" -> text.isBlank() ? "<br>" : "<p>" + text + "</p>";
                        case "linebreak" -> "<br>";
                        case "heading" -> switch (block.level()) {
                            case 1 -> "<h2>" + text + "</h2>";
                            case 2 -> "<h3>" + text + "</h3>";
                            case 3 -> "<h4>" + text + "</h4>";
                            default -> "<p>" + text + "</p>";
                        };
                        case "list" -> "<ul>" + text + "</ul>";
                        default -> "<p>" + text + "</p>";
                    };
                })
                .collect(Collectors.joining());

        return Jsoup.clean(html, Safelist.none()
                .addTags("p", "h2", "h3", "h4", "strong", "em", "u", "s", "code", "a", "ul", "ol", "li", "br")
        );
    }
}