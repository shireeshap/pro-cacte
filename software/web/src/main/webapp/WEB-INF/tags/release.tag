        <c:if test="${crf.status.displayName ne 'Released'}">
            <tags:button color="blue" markupWithTag="a" icon="window" value="Release Form"
                         onclick="javascript:releaseForm('${crf.id}')"/>
        </c:if>