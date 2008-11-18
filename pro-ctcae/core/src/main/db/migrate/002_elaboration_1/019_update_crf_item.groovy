class UpdateCrfItem extends edu.northwestern.bioinformatics.bering.Migration {
  void up() {
    addColumn('CRF_ITEMS', 'response_required', 'boolean', nullable: true)
    addColumn('CRF_ITEMS', 'instructions', 'string', nullable: true)
    addColumn('CRF_ITEMS', 'allignment', 'string', nullable: true)
  }

  void down() {
  }
}
