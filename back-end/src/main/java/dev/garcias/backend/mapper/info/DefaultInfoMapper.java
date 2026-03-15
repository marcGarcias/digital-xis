package dev.garcias.backend.mapper.info;

import dev.garcias.backend.dto.cms.CMSInfoDTO;
import dev.garcias.backend.dto.response.info.InfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", implementationName = "defaultInfoMapperImpl")
public interface DefaultInfoMapper {

    @Mapping(target = "email", expression = "java(cmsInfoDTO.email())")
    @Mapping(target = "cnpj", expression = "java(cmsInfoDTO.cnpj())")
    @Mapping(target = "copyrightYear", expression = "java(cmsInfoDTO.anoDoCopyrights())")
    @Mapping(target = "whatsappRaw", expression = "java(formatWhatsappRaw(cmsInfoDTO.whatsapp()))")
    @Mapping(target = "whatsappFormatted", expression = "java(formatWhatsapp(cmsInfoDTO.whatsapp()))")
    @Mapping(target = "instagramFormatted", expression = "java(formatInstagramHandle(cmsInfoDTO.instagram()))")
    @Mapping(target = "instagramRaw", expression = "java(formatInstagramUsername(cmsInfoDTO.instagram()))")
    InfoResponse toInfoResponse(CMSInfoDTO cmsInfoDTO);

    default String formatWhatsappRaw(String whatsapp) {
        if (whatsapp == null) return null;
        return whatsapp.trim().replaceAll("\\s+", "");
    }

    default String formatWhatsapp(String whatsapp) {
        if (whatsapp == null) return null;
        String digits = whatsapp.trim().replaceAll("\\D", "");
        if (digits.length() == 13) {
            return String.format("(%s) %s-%s",
                    digits.substring(2, 4),
                    digits.substring(4, 9),
                    digits.substring(9)
            );
        }
        return digits;
    }

    default String formatInstagramHandle(String instagram) {
        if (instagram == null) return null;
        String clean = instagram.trim();
        return clean.startsWith("@") ? clean : "@" + clean;
    }

    default String formatInstagramUsername(String instagram) {
        if (instagram == null) return null;
        String clean = instagram.trim();
        return clean.startsWith("@") ? clean.substring(1) : clean;
    }
}